package com.ticketPing.order.application.service;

import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.ORDER_ALREADY_EXIST;
import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.ORDER_ALREADY_OCCUPIED;
import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.ORDER_NOT_FOUND;

import java.time.LocalDateTime;
import java.util.Random;
import com.ticketPing.order.application.dtos.OrderCreateRequestDto;
import com.ticketPing.order.application.dtos.OrderCreateResponseDto;
import com.ticketPing.order.client.PerformanceClient;
import com.ticketPing.order.domain.entity.Order;
import com.ticketPing.order.domain.entity.OrderSeatRedis;
import com.ticketPing.order.domain.entity.OrderStatus;
import com.ticketPing.order.domain.repository.OrderRepository;
import com.ticketPing.order.domain.repository.OrderSeatRedisRepository;
import com.ticketPing.order.presentation.response.exception.OrderExceptionCase;
import dto.OrderPerformanceDto;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository; // RDB에 대한 리포지토리
    private final OrderSeatRedisRepository orderSeatRedisRepository; // Redis 리포지토리
    private final PerformanceClient performanceClient;
    private final RedissonClient redissonClient;

    public void saveOrderSeat(Order order) {
        orderRepository.save(order);
    }

    public void savePerformanceOrderToRedis(UUID performanceHallId) {
        // 공연장 이름 조회
        String performanceHallName = performanceClient.getPerformanceHallName(performanceHallId);

        // Order에서 해당 공연장 이름을 가진 주문이 있는지 확인
        List<Order> existingOrders = orderRepository.findAllByPerformanceHall(performanceHallName);
        if (!existingOrders.isEmpty()) {
            throw new RuntimeException(ORDER_ALREADY_EXIST.getMessage());
        }

        List<OrderPerformanceDto> orderPerformanceDtoList = performanceClient.getHallSeatsByPerformanceHallId(
            performanceHallId);

        // RDB에 Order 저장
        for (OrderPerformanceDto orderPerformanceDto : orderPerformanceDtoList) {
            // Order 객체 생성
            Order order = mapToOrder(orderPerformanceDto);
            saveOrderSeat(order); // Order 저장

            OrderSeatRedis orderSeatRedis = mapToOrderSeatRedis(order);

            // Redis에 저장
            orderSeatRedisRepository.save(orderSeatRedis);

        }


    }

    private OrderSeatRedis mapToOrderSeatRedis(Order order) {
        return OrderSeatRedis.builder()
            .id(order.getId())
            .orderCancelled(order.isOrderCancelled())
            .reservationDate(order.getReservationDate())
            .userId(order.getUserId())
            .scheduleId(order.getScheduleId())
            .companyId(order.getCompanyId())
            .rowNumber(order.getRowNumber())
            .seatNumber(order.getSeatNumber())
            .seatGrade(order.getSeatGrade())
            .price(order.getPrice())
            .performanceHall(order.getPerformanceHall())
            .startTime(order.getStartTime())
            .endTime(order.getEndTime())
            .performanceDate(order.getPerformanceDate())
            .totalSeats(order.getTotalSeats())
            .orderStatus(order.getOrderStatus()) // OrderStatus 매핑
            .performanceName(order.getPerformanceName())
            .build();
    }

    private Order mapToOrder(OrderPerformanceDto dto) {
        return Order.builder()
            .orderCancelled(dto.isOrderCancelled())
            .reservationDate(dto.getReservationDate())
            .userId(dto.getUserId())
            .scheduleId(dto.getScheduleId())
            .companyId(dto.getCompanyId())
            .rowNumber(dto.getSeatRow())
            .seatNumber(dto.getSeatColumn())
            .seatGrade(dto.getSeatRate())
            .price(dto.getPrice())
            .performanceHall(dto.getPerformanceHall())
            .startTime(dto.getStartTime())
            .endTime(dto.getEndTime())
            .performanceDate(dto.getPerformanceDate())
            .totalSeats(dto.getTotalSeats())
            .orderStatus(OrderStatus.NO_RESERVATION) // 기본값 설정
            .performanceName(dto.getPerformanceName())
            .build();
    }

    @Transactional
    public OrderCreateResponseDto orderPerformanceSeats(OrderCreateRequestDto orderCreateRequestDto) {
        // Redis에서 OrderSeatRedis 조회
        OrderSeatRedis orderSeatRedis = orderSeatRedisRepository.findById(
                orderCreateRequestDto.orderId())
            .orElseThrow(() -> new RuntimeException(OrderExceptionCase.ORDER_NOT_FOUND.getMessage()));

        orderSeatRedis.setUserId(UUID.fromString("3f1b7f0a-7c8a-4b2e-bb5d-986f9c7c8b45"));

        // 분산 락 설정
        RLock lock = redissonClient.getLock("orderLock:" + orderCreateRequestDto.orderId());
        try {
            // 락 시도
            if (lock.tryLock(1, 10, TimeUnit.SECONDS)) { // 1초 대기 후 10초 동안 락 유지
                try {
                    // OrderStatus 확인
                    if (!orderSeatRedis.getOrderStatus().equals(OrderStatus.NO_RESERVATION)) {
                        throw new RuntimeException(OrderExceptionCase.ORDER_ALREADY_OCCUPIED.getMessage() + ", 상태정보 = "
                            + orderSeatRedis.getOrderStatus());
                    }

                    // 비즈니스 로직 실행
                    orderSeatRedis.setOrderStatus(OrderStatus.PENDING);
                    System.out.println("결제로직 호출");

                    // 결제 로직의 성공 여부에 따라 상태 업데이트
                    Random random = new Random();
                    boolean paymentSuccess = random.nextBoolean(); // 결제 성공 여부를 체크하는 로직 추가

                    if (paymentSuccess) {
                        orderSeatRedis.setReservationDate(LocalDateTime.now());
                        orderSeatRedis.setOrderStatus(OrderStatus.RESERVATION);
                    } else {
                        orderSeatRedis.setReservationDate(null);
                        orderSeatRedis.setOrderStatus(OrderStatus.NO_RESERVATION);
                    }
                    orderSeatRedisRepository.save(orderSeatRedis);

                    return orderSeatRedis.OrderSeatRedisToDto();

                } finally {
                    lock.unlock(); // 락 해제
                }
            } else {
                throw new RuntimeException(OrderExceptionCase.ORDER_ALREADY_OCCUPIED.getMessage());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 스레드 인터럽트 상태 복원
            throw new RuntimeException(OrderExceptionCase.LOCK_ACQUISITION_INTERRUPTED.getMessage()); // 사용자 정의 예외로 변경
        }

    }

}

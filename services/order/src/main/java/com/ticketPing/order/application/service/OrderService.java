package com.ticketPing.order.application.service;

import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.ORDER_ALREADY_OCCUPIED;
import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.ORDER_FOR_PERFORMANCE_CACHE_NOT_FOUND;
import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.ORDER_NOT_FOUND;
import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.ORDER_NOT_FOUND_AT_REDIS;
import static com.ticketPing.order.presentation.response.exception.OrderExceptionCase.ORDER_STATUS_UNKNOWN;
import static com.ticketPing.order.presentation.response.success.OrderSuccessCase.ORDER_OCCUPYING_SEAT_SUCCESS;

import com.ticketPing.order.application.dtos.OrderCreateDto;
import com.ticketPing.order.client.PerformanceClient;
import com.ticketPing.order.domain.entity.Order;
import com.ticketPing.order.domain.entity.OrderSeatRedis;
import com.ticketPing.order.domain.entity.OrderStatus;
import com.ticketPing.order.domain.events.OrderCompletedEvent;
import com.ticketPing.order.domain.repository.OrderRepository;
import com.ticketPing.order.domain.repository.OrderSeatRedisRepository;
import com.ticketPing.order.domain.repository.OrderSeatRepository;
import com.ticketPing.order.presentation.response.exception.OrderExceptionCase;
import common.response.CommonResponse;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository; // RDB에 대한 리포지토리
    private final OrderSeatRedisRepository orderSeatRedisRepository; // Redis 리포지토리
    private final OrderSeatRepository orderSeatRepository;
    private final PerformanceClient performanceClient;
    private final RedissonClient redissonClient;
    private final RedisOperator redisOperator;
    private final EventApplicationService eventApplicationService;

    private final static int SEAT_LOCK_CACHE_EXPIRE_SECONDS = 330;

    @Transactional
    public CommonResponse<Void> orderOccupyingSeats(OrderCreateDto orderCreateRequestDto) {

        UUID tempUserId = UUID.fromString("3f1b7f0a-7c8a-4b2e-bb5d-986f9c7c8b45"); // TODO : 실제 userId 값으로 변경
        String seatId = orderCreateRequestDto.seatId().toString();
        String userSeatId = tempUserId + ":" + seatId.toString();

        log.info("Checking existence of key: {}", seatId);
        if (!redisOperator.exists(seatId)) {
            log.error("Key not found in Redis: {}", seatId);
            return CommonResponse.error(ORDER_FOR_PERFORMANCE_CACHE_NOT_FOUND);
        }

        // Redis에서 값을 가져와서 OrderStatus로 변환
        OrderStatus currentStatus = getOrderStatusFromRedis(seatId);
        if (currentStatus == OrderStatus.OCCUPIED) {
            return CommonResponse.error(ORDER_ALREADY_OCCUPIED);
        }

        Optional<OrderSeatRedis> orderSeatRedis = orderSeatRedisRepository.findById(userSeatId);
        if (orderSeatRedis.isPresent()) {
            return CommonResponse.error(ORDER_ALREADY_OCCUPIED);
        }

        // 분산 락 설정
        return redLockForSeat(userSeatId, seatId);
    }

    public CommonResponse<Void> redLockForSeat(String userSeatId, String seatId) {
        String lockKey = "lock:" + seatId;
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 락 시도
            if (lock.tryLock(1, 10, TimeUnit.SECONDS)) { // 1초 대기 후 10초 동안 락 유지
                try {
                    String userSeatIdWithTTL = userSeatId + "_TTL";
                    redisOperator.setIfAbsentWithTTL(userSeatIdWithTTL, userSeatId, SEAT_LOCK_CACHE_EXPIRE_SECONDS);
                    OrderStatus currentStatus = getOrderStatusFromRedis(seatId);

                    if (currentStatus == OrderStatus.NO_RESERVATION) {
                        redisOperator.set(seatId, OrderStatus.OCCUPIED.name()); // OCCUPIED 상태로 업데이트
                    }

                    return CommonResponse.success(ORDER_OCCUPYING_SEAT_SUCCESS);

                } finally {
                    lock.unlock(); // 락 해제
                }
            } else {
                throw new RuntimeException(ORDER_ALREADY_OCCUPIED.getMessage());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 스레드 인터럽트 상태 복원
            throw new RuntimeException(OrderExceptionCase.LOCK_ACQUISITION_INTERRUPTED.getMessage());
        }
    }

    private OrderStatus getOrderStatusFromRedis(String key) {
        String value = redisOperator.get(key);
        if (value == null) {
            return OrderStatus.NO_RESERVATION;
        }
        try {
            return OrderStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(ORDER_STATUS_UNKNOWN.getMessage());
        }
    }
//    @Transactional
//    public OrderCreateDto orderRequestPaymentForOccupiedSeats(
//        OrderCreateDto orderCreateRequestDto) {
//        OrderSeatRedis orderSeatRedis = orderSeatRedisRepository.findById(
//                orderCreateRequestDto.orderId())
//            .orElseThrow(
//                () -> new RuntimeException(OrderExceptionCase.ORDER_NOT_FOUND.getMessage()));
//
//        // TODO : 게이트웨이 userId와 orderSeatRedis.userId 비교하기
//
//        if (!orderSeatRedis.getOrderStatus().equals(OrderStatus.PENDING)) {
//            throw new RuntimeException(ORDER_SEATS_NOT_OCCUPIED.getMessage());
//        }
//
//        //TODO : 결제 요청 API 호출
//
//        return orderSeatRedis.OrderSeatRedisToDto();
//    }

    @Transactional
    public void updateOrderStatus(UUID orderId, String status) {
        OrderSeatRedis orderSeatRedis = orderSeatRedisRepository.findById(
                String.valueOf(orderId))
            .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND_AT_REDIS.getMessage()));

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new RuntimeException(ORDER_NOT_FOUND.getMessage()));
        // TODO : 게이트웨이 userId(현재 접속된 Id)와 orderSeatRedis.userId 비교하기

        //if (status.equals())

//        if (!orderSeatRedis.getOrderStatus()Id().equals(orderPaymentDto.userId())) {
//            //TODO : 결제 환불 API 호출
//
//            changeOrderStatusInRedisAndDb(orderSeatRedis, order, OrderStatus.NO_RESERVATION);
//
//            throw new RuntimeException(USER_ID_WITH_SEATS_NOT_MATCHED.getMessage());
//        }
//
//        if (!order.getId().equals(orderSeatRedis.getId())) {
//            //TODO : 결제 환불 API 호출
//
//            changeOrderStatusInRedisAndDb(orderSeatRedis, order, OrderStatus.NO_RESERVATION);
//
//            throw new RuntimeException(ORDER_EACH_USER_NOT_MATCHED.getMessage());
//        }

         changeOrderStatusInRedisAndDb(orderSeatRedis, order, OrderStatus.OCCUPIED);

        // 예매 완료 이벤트 발행
        String userId = "1";
        String performanceId = "1";
        eventApplicationService.publishOrderCompletedEvent(
            OrderCompletedEvent.create(userId, performanceId));

    }

    public void test() {
        // 예매 완료 이벤트 발행
        String userId = "1";
        String performanceId = "1";
        eventApplicationService.publishOrderCompletedEvent(OrderCompletedEvent.create(userId, performanceId));
    }

    public void changeOrderStatusInRedisAndDb(OrderSeatRedis orderSeatRedis, Order order,
        OrderStatus orderStatus) {
//        orderSeatRedis.setOrderStatus(orderStatus);
//        order.setOrderStatus(orderStatus);
//
//        if (orderStatus.equals(OrderStatus.RESERVATION)) {
//            order.setUserId(orderSeatRedis.getUserId());
//            order.setReservationDate(LocalDateTime.now());
//        }
//
//        orderSeatRedisRepository.save(orderSeatRedis);
//        orderRepository.save(order);
    }

//    public List<OrderCreateResponseDto> orderSeatsList() {
//        Iterable<OrderSeatRedis> orderSeatRedisIterable = orderSeatRedisRepository.findAll();
//
//        return StreamSupport.stream(orderSeatRedisIterable.spliterator(), false)
//            .map(OrderSeatRedis::OrderSeatRedisToDto)
//            .collect(Collectors.toList());
//    }
}

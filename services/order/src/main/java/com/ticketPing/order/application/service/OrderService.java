package com.ticketPing.order.application.service;

import dto.OrderPerformanceDto;
import com.ticketPing.order.client.PerformanceClient;
import com.ticketPing.order.domain.entity.Order;
import com.ticketPing.order.domain.entity.OrderSeatRedis;
import com.ticketPing.order.domain.entity.OrderStatus;
import com.ticketPing.order.domain.repository.OrderRepository;
import com.ticketPing.order.domain.repository.OrderSeatRedisRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository; // RDB에 대한 리포지토리
    private final OrderSeatRedisRepository orderSeatRedisRepository; // Redis 리포지토리
    private final PerformanceClient performanceClient;

    public void saveOrderSeat(Order order) {
        orderRepository.save(order);
    }

    public void savePerformanceOrderToRedis(UUID performanceHallId) {
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
            .build();
    }
}

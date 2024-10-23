package com.ticketPing.order.application.dtos;

import com.ticketPing.order.domain.model.entity.Order;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record OrderResponse(
    UUID id,
    Boolean orderStatus,
    LocalDateTime reservationDate,
    UUID userId,
    UUID scheduleId,
    UUID companyId,
    String performanceName,
    int row,
    int col,
    String seatGrade,
    int price
) {

    public static OrderResponse from(Order order) {
        return OrderResponse.builder()
            .id(order.getId())
            .row(order.getOrderSeat().getRow())
            .col(order.getOrderSeat().getCol())
            .price(order.getOrderSeat().getCost())
            .userId(order.getUserId())
            .performanceName(order.getPerformanceName())
            .companyId(order.getCompanyId())
            .orderStatus(order.getOrderStatus())
            .reservationDate(order.getReservationDate())
            .scheduleId(order.getScheduleId())
            .seatGrade(order.getOrderSeat().getSeatRate().toString())
            .build();
    }

}
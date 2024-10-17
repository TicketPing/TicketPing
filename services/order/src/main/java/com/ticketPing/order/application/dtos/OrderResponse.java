package com.ticketPing.order.application.dtos;

import com.ticketPing.order.domain.model.entity.Order;
import com.ticketPing.order.domain.model.entity.RedisSeat;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record OrderResponse(
    UUID id,
    Boolean orderStatus,
    LocalDateTime reservationDate, // 예매 생성 시간
    UUID userId, // 사용자 아이디
    UUID scheduleId, // 공연 일정 아이디
    UUID companyId, // 회사명
    String performanceName,
    int row, // 행번호
    int col, // 열번호
    String seatGrade, // 좌석등급
    int price
) {

    public static OrderResponse from(Order order, RedisSeat redisSeat) {
        return OrderResponse.builder()
            .id(order.getId())
            .row(order.getOrderSeat().getRow())
            .col(order.getOrderSeat().getCol())
            .price(order.getOrderSeat().getCost())
            .userId(order.getUserId())
            .performanceName(order.getPerformanceName())
            .companyId(order.getCompanyId())
            .orderStatus(redisSeat.getSeatState())
            .reservationDate(order.getReservationDate())
            .scheduleId(order.getScheduleId())
            .seatGrade(order.getOrderSeat().getSeatGrade())
            .build();
    }

}
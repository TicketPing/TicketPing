package com.ticketPing.order.application.dtos;

import com.ticketPing.order.domain.model.entity.OrderSeat;
import com.ticketPing.order.domain.model.entity.Order;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PROTECTED)
public class UserReservationDto {
    private UUID orderId;            // 주문 ID
    private Boolean orderStatus;     // 주문 상태
    private LocalDateTime reservationDate; // 예매 생성 시간
    private Boolean isOrderCancelled; // 예매 취소 여부
    private UUID userId;             // 사용자 ID
    private UUID scheduleId;         // 공연 일정 ID
    private UUID companyId;          // 회사 ID
    private String performanceName;   // 공연 이름
    private UUID seatId;             // 좌석 ID
    private int row;                 // 행번호
    private int col;                 // 열번호
    private String seatGrade;        // 좌석 등급
    private int cost;                // 가격

    // 생성자 또는 빌더 추가
    public static UserReservationDto from(Order order, OrderSeat orderSeat) {
        return UserReservationDto.builder()
            .orderId(order.getId())
            .orderStatus(order.getOrderStatus())
            .reservationDate(order.getReservationDate())
            .isOrderCancelled(order.getIsOrderCancelled())
            .userId(order.getUserId())
            .scheduleId(order.getScheduleId())
            .companyId(order.getCompanyId())
            .performanceName(order.getPerformanceName())
            .seatId(orderSeat.getSeatId())
            .row(orderSeat.getRow())
            .col(orderSeat.getCol())
            .seatGrade(orderSeat.getSeatGrade())
            .cost(orderSeat.getCost())
            .build();
    }
}


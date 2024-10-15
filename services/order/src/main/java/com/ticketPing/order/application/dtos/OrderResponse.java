package com.ticketPing.order.application.dtos;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record OrderResponse(
    UUID id,
    Boolean orderStatus,
    LocalDateTime reservationDate, // 예매 생성 시간
    UUID userId, // 사용자 아이디
    UUID scheduleId, // 공연 일정 아이디
    UUID companyId, // 회사명
    String performanceName,
    int rowNumber, // 행번호
    int columnNumber, // 열번호
    String seatGrade, // 좌석등급
    int price
) {

}

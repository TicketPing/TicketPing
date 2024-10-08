package com.ticketPing.order.application.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record OrderCreateResponseDto(
    String performanceHall,
    String performanceName,
    LocalTime startTime,
    LocalTime endTime,
    UUID orderId,
    LocalDate performanceDate,
    String orderStatus,
    int price,
    String seatGrade,
    UUID userId,
    LocalDateTime reservationDate,
    int rowNumber,
    int seatNumber
) {

}

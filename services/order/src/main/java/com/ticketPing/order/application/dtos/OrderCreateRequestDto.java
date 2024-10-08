package com.ticketPing.order.application.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public record OrderCreateRequestDto(
    String performanceHall,
    String performanceName,
    LocalTime startTime,
    LocalTime endTime,
    UUID orderId,
    LocalDate performanceDate
) {

}

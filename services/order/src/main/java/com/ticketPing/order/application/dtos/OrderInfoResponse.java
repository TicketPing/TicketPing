package com.ticketPing.order.application.dtos;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderInfoResponse(
    UUID ,
    Integer row,
    Integer col,
    Boolean seatState,
    String seatRate,
    Integer cost,
    UUID scheduleId,
    LocalDateTime startTime,
    UUID performanceHallId,
    String performanceHallName,
    UUID performanceId,
    String performanceName,
    Integer performanceGrade,
    UUID companyId
) {

}

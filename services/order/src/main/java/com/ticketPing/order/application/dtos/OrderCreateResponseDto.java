package com.ticketPing.order.application.dtos;

import com.ticketPing.order.application.dtos.mapper.ObjectMapperBasedVoMapper;
import com.ticketPing.order.domain.entity.OrderSeatRedis;
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
    public static OrderCreateResponseDto from(OrderSeatRedis orderSeatRedis) {
        return ObjectMapperBasedVoMapper.convert(orderSeatRedis, OrderCreateResponseDto.class);
    }

}

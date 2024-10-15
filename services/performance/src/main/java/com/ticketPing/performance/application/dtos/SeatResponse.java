package com.ticketPing.performance.application.dtos;

import com.ticketPing.performance.domain.entity.Seat;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record SeatResponse (
    UUID seatId,
    Integer row,
    Integer col,
    Boolean seatState,
    String seatRate,
    Integer cost
) {
    public static SeatResponse of(Seat seat) {
        return SeatResponse.builder()
                .seatId(seat.getId())
                .row(seat.getRow())
                .col(seat.getCol())
                .seatState(seat.getSeatState())
                .seatRate(seat.getSeatCost().getSeatRate().getValue())
                .cost(seat.getSeatCost().getCost())
                .build();
    }
}

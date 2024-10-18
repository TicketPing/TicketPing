package com.ticketPing.order.application.dtos.temp;

import java.util.UUID;

public record SeatResponse(
    UUID seatId,
    Integer row,
    Integer col,
    Boolean seatState,
    String seatRate,
    Integer cost
) {

}


package com.ticketPing.order.application.dtos.temp;

import lombok.Data;

import java.util.UUID;

@Data
public class SeatResponse {
    UUID seatId;
    Integer row;
    Integer col;
    Boolean seatState;
    String seatRate;
    Integer cost;

    public void updateSeatState(Boolean seatState) {
        this.seatState = seatState;
    }
}


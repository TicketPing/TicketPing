package com.ticketPing.order.application.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class OrderSeatInfo {
    private String seatId;
    private int row;
    private int col;
    @Builder.Default
    private boolean seatState = false;
    private String seatRate;
    private int cost;
}


package com.ticketPing.order.application.dtos;

import com.ticketPing.order.domain.model.entity.RedisSeat;
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

    public static OrderSeatInfo from(RedisSeat redisSeat) {
        return OrderSeatInfo.builder()
            .seatId(redisSeat.getSeatId())
            .row(redisSeat.getRow())
            .col(redisSeat.getCol())
            .seatState(redisSeat.getSeatState())
            .seatRate(String.valueOf(redisSeat.getSeatRate()))
            .cost(redisSeat.getCost())
            .build();
    }

    public void updateSeatState(boolean seatState) {
        this.seatState = seatState;
    }
}


package com.ticketPing.performance.domain.entity;

import com.ticketPing.performance.domain.entity.enums.SeatRate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Data
@Builder(access = AccessLevel.PRIVATE)
@RedisHash("seat")
public class RedisSeat {
    @Id
    private UUID seatId;
    private Integer row;
    private Integer col;
    private Boolean seatState;
    private SeatRate seatRate;
    private Integer cost;

    public static RedisSeat from(Seat seat) {
        return RedisSeat.builder()
                .seatId(seat.getId())
                .row(seat.getRow())
                .col(seat.getCol())
                .seatState(seat.getSeatState())
                .seatRate(seat.getSeatCost().getSeatRate())
                .cost(seat.getSeatCost().getCost())
                .build();
    }
}

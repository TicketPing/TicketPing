package com.ticketPing.performance.domain.entity;

import com.ticketPing.performance.domain.entity.enums.SeatRate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder(access = AccessLevel.PRIVATE)
@RedisHash(value = "seat:")
public class RedisSeat {
    // TODO: id 넣는 다른 방법?
    @Id
    private String id;
    private Integer row;
    private Integer col;
    private Boolean seatState;
    private SeatRate seatRate;
    private Integer cost;

    public static RedisSeat from(Seat seat) {
        return RedisSeat.builder()
                .id(seat.getSchedule().getId() + ":" + seat.getId())
                .row(seat.getRow())
                .col(seat.getCol())
                .seatState(seat.getSeatState())
                .seatRate(seat.getSeatCost().getSeatRate())
                .cost(seat.getSeatCost().getCost())
                .build();
    }
}

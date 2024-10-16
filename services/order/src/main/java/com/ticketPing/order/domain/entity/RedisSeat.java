package com.ticketPing.order.domain.entity;

import com.ticketPing.order.application.dtos.temp.SeatRate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@RedisHash(value = "seat")
public class RedisSeat {

    @Id
    private String seatId;
    private Integer row;
    private Integer col;
    private Boolean seatState;
    private SeatRate seatRate;
    private Integer cost;

}


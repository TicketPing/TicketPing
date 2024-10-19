package com.ticketPing.order.domain.model.entity;

import com.ticketPing.order.application.dtos.temp.SeatRate;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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


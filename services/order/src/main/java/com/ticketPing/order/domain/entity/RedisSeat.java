package com.ticketPing.order.domain.entity;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@Builder
@RedisHash("seat")
public class RedisSeat {

    @Id
    private UUID seatId;
    private Integer row;
    private Integer col;
    private Boolean seatState;
    private String seatRate;
    private Integer cost;

}


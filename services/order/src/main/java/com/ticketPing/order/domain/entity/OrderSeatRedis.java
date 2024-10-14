package com.ticketPing.order.domain.entity;


import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("order_seat")
public class OrderSeatRedis {

    @Id
    private String id; // 좌석 ID로 변경

    private Boolean orderStatus;

}



package com.ticketPing.order.domain.repository;

import com.ticketPing.order.domain.entity.OrderSeatRedis;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface OrderSeatRedisRepository extends CrudRepository<OrderSeatRedis, UUID> {
}


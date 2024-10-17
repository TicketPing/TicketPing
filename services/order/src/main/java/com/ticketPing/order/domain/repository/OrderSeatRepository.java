package com.ticketPing.order.domain.repository;

import com.ticketPing.order.domain.model.entity.OrderSeat;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface OrderSeatRepository extends CrudRepository<OrderSeat, UUID> {

}

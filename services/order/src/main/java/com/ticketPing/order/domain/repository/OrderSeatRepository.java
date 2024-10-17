package com.ticketPing.order.domain.repository;

import com.ticketPing.order.domain.model.entity.OrderSeat;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderSeatRepository extends JpaRepository<OrderSeat, UUID> {

}

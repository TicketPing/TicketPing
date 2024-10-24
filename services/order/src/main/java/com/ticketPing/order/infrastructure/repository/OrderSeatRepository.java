package com.ticketPing.order.infrastructure.repository;

import com.ticketPing.order.domain.model.entity.OrderSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderSeatRepository extends JpaRepository<OrderSeat, UUID> {

}
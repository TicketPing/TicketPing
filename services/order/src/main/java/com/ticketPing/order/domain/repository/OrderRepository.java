package com.ticketPing.order.domain.repository;

import com.ticketPing.order.domain.entity.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByRowNumberAndSeatNumber(int rowNumber, int seatNumber);
}

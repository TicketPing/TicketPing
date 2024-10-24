package com.ticketPing.order.infrastructure.repository;

import com.ticketPing.order.domain.model.entity.Order;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByUserId(UUID userId);

    List<Order> findByScheduleIdAndOrderSeatSeatId(UUID seatId, UUID scheduleId);
}

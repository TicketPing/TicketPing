package com.ticketPing.order.domain.repository;

import com.ticketPing.order.domain.model.entity.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}

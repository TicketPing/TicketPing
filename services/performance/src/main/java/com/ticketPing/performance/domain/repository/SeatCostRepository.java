package com.ticketPing.performance.domain.repository;

import com.ticketPing.performance.domain.entity.SeatCost;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatCostRepository extends JpaRepository<SeatCost, UUID> {

}

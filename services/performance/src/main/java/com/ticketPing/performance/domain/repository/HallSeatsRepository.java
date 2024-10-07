package com.ticketPing.performance.domain.repository;

import com.ticketPing.performance.domain.entity.HallSeats;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HallSeatsRepository extends JpaRepository<HallSeats, UUID> {
    List<HallSeats> findByPerformanceHallId(UUID performanceHallId);
}

package com.ticketPing.performance.infrastructure.repository;

import com.ticketPing.performance.domain.entity.Seat;
import com.ticketPing.performance.domain.repository.SeatRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SeatJpaRepository extends SeatRepository, JpaRepository<Seat, UUID> {
}

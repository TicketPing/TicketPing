package com.ticketPing.performance.infrastructure.repository;

import com.ticketPing.performance.domain.entity.Schedule;
import com.ticketPing.performance.domain.entity.Seat;
import com.ticketPing.performance.domain.repository.SeatRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface SeatJpaRepository extends SeatRepository, JpaRepository<Seat, UUID> {
    @Query(value = "select s from Seat s " +
            "join fetch s.seatCosts sc " +
            "where s.schedule=:schedule")
    List<Seat> findByScheduleJoinSeatCost(Schedule schedule);
}

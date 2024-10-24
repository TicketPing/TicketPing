package com.ticketPing.performance.infrastructure.repository;

import com.ticketPing.performance.domain.model.entity.Schedule;
import com.ticketPing.performance.domain.model.entity.Seat;
import com.ticketPing.performance.domain.repository.SeatRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatJpaRepository extends SeatRepository, JpaRepository<Seat, UUID> {
    @Query(value = "select s from Seat s " +
            "join fetch s.seatCost sc " +
            "where s.id=:seatId")
    Optional<Seat> findByIdJoinSeatCost(UUID seatId);

    @Query(value = "select s from Seat s " +
            "join fetch s.seatCost sc " +
            "where s.schedule=:schedule")
    List<Seat> findByScheduleJoinSeatCost(Schedule schedule);

    @Query(value = "select s from Seat s " +
            "join fetch s.seatCost sc " +
            "join fetch s.schedule sd " +
            "join fetch sd.performance p " +
            "join fetch sd.performanceHall ph " +
            "where s.id=:seatId")
    Optional<Seat> findByIdJoinAll(UUID seatId);
}

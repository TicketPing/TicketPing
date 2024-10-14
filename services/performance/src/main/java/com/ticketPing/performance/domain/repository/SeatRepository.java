package com.ticketPing.performance.domain.repository;

import com.ticketPing.performance.domain.entity.Schedule;
import com.ticketPing.performance.domain.entity.Seat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository {
    Seat save(Seat seat);

    Optional<Seat> findById(UUID id);

    List<Seat> findByScheduleJoinSeatCost(Schedule schedule);

    Optional<Seat> findByIdJoinAll(UUID seatId);
}

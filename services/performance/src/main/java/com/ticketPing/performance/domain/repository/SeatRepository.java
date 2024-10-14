package com.ticketPing.performance.domain.repository;

import com.ticketPing.performance.domain.entity.Seat;

public interface SeatRepository {
    Seat save(Seat seat);
}

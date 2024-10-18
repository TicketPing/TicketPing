package com.ticketPing.performance.domain.repository;

import com.ticketPing.performance.domain.entity.PerformanceHall;

public interface PerformanceHallRepository {
    PerformanceHall save(PerformanceHall performanceHall);

    long count();
}

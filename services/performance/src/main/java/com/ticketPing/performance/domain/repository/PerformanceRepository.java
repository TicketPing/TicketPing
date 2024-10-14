package com.ticketPing.performance.domain.repository;

import com.ticketPing.performance.domain.entity.Performance;

public interface PerformanceRepository {
    Performance save(Performance performance);
}

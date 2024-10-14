package com.ticketPing.performance.domain.repository;

import com.ticketPing.performance.domain.entity.Performance;
import com.ticketPing.performance.domain.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);

    Optional<Schedule> findById(UUID id);

    Page<Schedule> findByPerformance(Performance performance, Pageable pageable);
}

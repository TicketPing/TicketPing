package com.ticketPing.performance.domain.repository;

import com.ticketPing.performance.domain.model.entity.Performance;
import com.ticketPing.performance.domain.model.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);

    Optional<Schedule> findById(UUID id);

    Page<Schedule> findByPerformance(Performance performance, Pageable pageable);

    List<Schedule> findByPerformance(Performance performance);
}

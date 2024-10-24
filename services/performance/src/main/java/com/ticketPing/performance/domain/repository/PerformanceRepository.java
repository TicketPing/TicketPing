package com.ticketPing.performance.domain.repository;

import com.ticketPing.performance.domain.model.entity.Performance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PerformanceRepository {
    Performance save(Performance performance);

    Optional<Performance> findById(UUID id);

    Page<Performance> findAll(Pageable pageable);
}

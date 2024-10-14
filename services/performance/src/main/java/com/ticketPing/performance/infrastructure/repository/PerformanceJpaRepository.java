package com.ticketPing.performance.infrastructure.repository;

import com.ticketPing.performance.domain.entity.Performance;
import java.util.UUID;

import com.ticketPing.performance.domain.repository.PerformanceRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceJpaRepository extends PerformanceRepository, JpaRepository<Performance, UUID> {

}

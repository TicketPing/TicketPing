package com.ticketPing.performance.infrastructure.repository;

import com.ticketPing.performance.domain.model.entity.PerformanceHall;
import java.util.UUID;

import com.ticketPing.performance.domain.repository.PerformanceHallRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceHallJpaRepository extends PerformanceHallRepository, JpaRepository<PerformanceHall, UUID> {

}

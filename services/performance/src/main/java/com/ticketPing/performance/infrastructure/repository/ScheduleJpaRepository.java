package com.ticketPing.performance.infrastructure.repository;

import com.ticketPing.performance.domain.entity.Schedule;
import com.ticketPing.performance.domain.repository.ScheduleRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ScheduleJpaRepository extends ScheduleRepository, JpaRepository<Schedule, UUID> {
}

package com.ticketPing.performance.domain.repository;

import com.ticketPing.performance.domain.entity.PerformanceSchedule;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerformanceScheduleRepository extends JpaRepository<PerformanceSchedule, UUID> {

    List<PerformanceSchedule> findAllByScheduledDate(LocalDate scheduledDate);

    Optional<PerformanceSchedule> findByPerformanceHallId(UUID performanceHallId);
}

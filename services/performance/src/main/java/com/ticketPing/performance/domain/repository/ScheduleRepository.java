package com.ticketPing.performance.domain.repository;

import com.ticketPing.performance.domain.entity.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);
}

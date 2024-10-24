package com.ticketPing.performance.application.service;

import com.ticketPing.performance.application.dtos.ScheduleResponse;
import com.ticketPing.performance.domain.model.entity.Performance;
import com.ticketPing.performance.domain.model.entity.Schedule;
import com.ticketPing.performance.domain.repository.ScheduleRepository;
import com.ticketPing.performance.presentation.cases.exception.ScheduleExceptionCase;
import common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ScheduleResponse getSchedule(UUID id) {
        Schedule schedule = findScheduleById(id);

        LocalDateTime cur = LocalDateTime.now();
        if(schedule.getPerformance().getReservationStartDate().isAfter(cur)
                || schedule.getPerformance().getReservationEndDate().isBefore(cur)) {
            throw new ApplicationException(ScheduleExceptionCase.NOT_RESERVATION_DATE);
        }

        return ScheduleResponse.of(schedule);
    }

    @Transactional
    public Schedule findScheduleById(UUID id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ScheduleExceptionCase.SCHEDULE_NOT_FOUND));
    }

    @Transactional
    public Page<ScheduleResponse> getSchedulesByPerformance(Performance performance, Pageable pageable) {
        Page<Schedule> schedules = scheduleRepository.findByPerformance(performance, pageable);
        return schedules.map(ScheduleResponse::of);
    }

    public List<Schedule> finadAllScheduleByPerformance(Performance performance) {
        List<Schedule> schedules = scheduleRepository.findByPerformance(performance);
        if(schedules.isEmpty()) {
            throw new ApplicationException(ScheduleExceptionCase.SCHEDULE_NOT_FOUND);
        }
        return schedules;
    }
}

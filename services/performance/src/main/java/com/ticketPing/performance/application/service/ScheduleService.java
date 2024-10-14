package com.ticketPing.performance.application.service;

import com.ticketPing.performance.application.dtos.ScheduleResponse;
import com.ticketPing.performance.domain.entity.Schedule;
import com.ticketPing.performance.domain.repository.ScheduleRepository;
import com.ticketPing.performance.presentation.cases.exception.ScheduleExceptionCase;
import common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Transactional
    public ScheduleResponse getSchedule(UUID id) {
        Schedule schedule = findScheduleById(id);

        LocalDateTime cur = LocalDateTime.now().plusDays(13);
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
}

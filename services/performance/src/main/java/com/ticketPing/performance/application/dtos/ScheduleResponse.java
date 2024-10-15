package com.ticketPing.performance.application.dtos;

import com.ticketPing.performance.domain.entity.Schedule;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record ScheduleResponse (
        UUID id,
        LocalDateTime startTime,
        UUID performanceHallId,
        UUID performanceId
){
    public static ScheduleResponse of(Schedule schedule) {
        return ScheduleResponse.builder()
            .id(schedule.getId())
            .startTime(schedule.getStartTime())
            .performanceHallId(schedule.getPerformanceHall().getId())
            .performanceId(schedule.getPerformance().getId())
            .build();
    }
}



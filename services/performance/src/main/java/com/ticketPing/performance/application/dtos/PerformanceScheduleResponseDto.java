package com.ticketPing.performance.application.dtos;

import com.ticketPing.performance.domain.entity.PerformanceSchedule;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class PerformanceScheduleResponseDto {

    private UUID id;                         // 일정 ID
    private LocalTime startTime;            // 공연 시작 시간
    private LocalTime endTime;              // 공연 종료 시간
    private LocalDate scheduledDate;        // 예정된 날짜
    private String performanceHallName; // 공연장 정보
    private String performanceName; // 공연 정보

    public static PerformanceScheduleResponseDto of(PerformanceSchedule performanceSchedule) {
        return PerformanceScheduleResponseDto.builder()
            .id(performanceSchedule.getId())
            .startTime(performanceSchedule.getStartTime())
            .endTime(performanceSchedule.getEndTime())
            .scheduledDate(performanceSchedule.getScheduledDate())
            .performanceHallName(performanceSchedule.getPerformanceHall().getName())
            .performanceName(performanceSchedule.getPerformance().getName())
            .build();
    }
}



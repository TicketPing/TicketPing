package com.ticketPing.performance.application.dtos;

import com.ticketPing.performance.domain.entity.Performance;
import com.ticketPing.performance.domain.entity.PerformanceHall;
import com.ticketPing.performance.domain.entity.Schedule;
import com.ticketPing.performance.domain.entity.Seat;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder(access = AccessLevel.PRIVATE)
public record OrderInfoResponse(
        UUID seatId,
        Integer row,
        Integer col,
        Boolean seatState,
        String seatRate,
        Integer cost,
        UUID scheduleId,
        LocalDateTime startTime,
        UUID performanceHallId,
        String performanceHallName,
        UUID performanceId,
        String performanceName,
        Integer performanceGrade,
        UUID companyId
) {
    public static OrderInfoResponse of(Seat seat) {
        Schedule schedule = seat.getSchedule();
        PerformanceHall performanceHall = schedule.getPerformanceHall();
        Performance performance = schedule.getPerformance();

        return OrderInfoResponse.builder()
                .seatId(seat.getId())
                .row(seat.getRow())
                .col(seat.getCol())
                .seatState(seat.getSeatState())
                .seatRate(seat.getSeatCost().getSeatRate().getValue())
                .cost(seat.getSeatCost().getCost())
                .scheduleId(schedule.getId())
                .startTime(schedule.getStartTime())
                .performanceHallId(performanceHall.getId())
                .performanceHallName(performanceHall.getName())
                .performanceId(performance.getId())
                .performanceName(performance.getName())
                .performanceGrade(performance.getGrade())
                .companyId(performance.getCompanyId())
                .build();
    }
}

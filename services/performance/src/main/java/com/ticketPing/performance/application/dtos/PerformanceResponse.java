package com.ticketPing.performance.application.dtos;

import com.ticketPing.performance.domain.entity.Performance;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;

@Builder(access = AccessLevel.PRIVATE)
public record PerformanceResponse(
        UUID id,
        String name,
        LocalDateTime reservationStartDate,
        LocalDateTime reservationEndDate,
        LocalDate startDate,
        LocalDate endDate,
        Integer grade,
        UUID companyId
){
    public static PerformanceResponse of(Performance performance) {
        return PerformanceResponse.builder()
            .id(performance.getId())
            .name(performance.getName())
            .reservationStartDate(performance.getReservationStartDate())
            .reservationEndDate(performance.getReservationEndDate())
            .startDate(performance.getStartDate())
            .endDate(performance.getEndDate())
            .grade(performance.getGrade())
            .companyId(performance.getCompanyId())
            .build();
    }
}

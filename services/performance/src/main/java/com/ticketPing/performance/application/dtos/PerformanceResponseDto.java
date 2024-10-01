package com.ticketPing.performance.application.dtos;

import com.ticketPing.performance.domain.entity.Performance;
import com.ticketPing.performance.domain.entity.SeatRate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder(access = AccessLevel.PRIVATE)
public class PerformanceResponseDto {

    private UUID id;                               // 공연 ID
    private String name;                           // 공연 이름
    private LocalDateTime reservationStartDate;    // 예약 시작일
    private LocalDateTime reservationEndDate;      // 예약 종료일
    private LocalDate startDate;                   // 공연 시작일
    private LocalDate endDate;                     // 공연 종료일
    private String grade;                          // 공연 등급
    private List<SeatCostResponseDto> seatCosts;   // 공연 가격
    private UUID companyId;                        // 회사 ID

    public static PerformanceResponseDto of(Performance performance) {
        List<SeatCostResponseDto> seatCostResponseDtos = performance.getSeatCosts().stream()
            .map(seatCost -> new SeatCostResponseDto(seatCost.getId(), seatCost.getSeatRate(),
                seatCost.getCost()))
            .collect(Collectors.toList());

        return PerformanceResponseDto.builder()
            .id(performance.getId())
            .name(performance.getName())
            .reservationStartDate(performance.getReservationStartDate())
            .reservationEndDate(performance.getReservationEndDate())
            .startDate(performance.getStartDate())
            .endDate(performance.getEndDate())
            .grade(performance.getGrade())
            .seatCosts(seatCostResponseDtos)
            .companyId(performance.getCompanyId())
            .build();
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class SeatCostResponseDto {

        private UUID id;
        private SeatRate seatRate;
        private Integer cost;
    }
}

package com.ticketPing.performance.application.dtos;

import com.ticketPing.performance.domain.entity.PerformanceHall;
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
public class PerformanceHallResponseDto {

    private UUID id;                // 공연장 ID
    private String name;           // 공연장 이름
    private String address;        // 공연장 주소
    private int totalSeats;        // 총 좌석 수
    private int rows;              // 행 수
    private int columns;            // 열 수

    public static PerformanceHallResponseDto of(PerformanceHall performanceHall) {
        return PerformanceHallResponseDto.builder()
            .id(performanceHall.getId())
            .name(performanceHall.getName())
            .address(performanceHall.getAddress())
            .totalSeats(performanceHall.getTotalSeats())
            .rows(performanceHall.getRows())
            .columns(performanceHall.getColumns())
            .build();
    }
}


package com.ticketPing.performance.application.service;

import com.ticketPing.performance.application.dtos.PerformanceResponseDto;
import com.ticketPing.performance.application.dtos.PerformanceScheduleResponseDto;
import com.ticketPing.performance.domain.entity.Performance;
import com.ticketPing.performance.domain.entity.PerformanceSchedule;
import com.ticketPing.performance.domain.repository.PerformanceRepository;
import com.ticketPing.performance.domain.repository.PerformanceScheduleRepository;
import com.ticketPing.performance.presentation.response.exception.PerformanceExceptionCase;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;
    private final PerformanceScheduleRepository performanceScheduleRepository;

    public PerformanceResponseDto getPerformance(UUID id) {
        Performance performance = performanceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                PerformanceExceptionCase.PERFORMANCE_NOT_FOUND.getMessage()));
        return PerformanceResponseDto.of(performance);
    }

    public List<PerformanceResponseDto> getAllPerformances() {
        List<Performance> performances = performanceRepository.findAll();

        return performances.stream().map(
            PerformanceResponseDto::of).toList();
    }

    public List<PerformanceScheduleResponseDto> getAllPerformancesByDate(LocalDate localDate) {
        List<PerformanceSchedule> performanceScheduleList = performanceScheduleRepository.findAllByScheduledDate(
            localDate);

        return performanceScheduleList.stream().map(PerformanceScheduleResponseDto::of).toList();
    }
}


package com.ticketPing.performance.application.service;

import com.ticketPing.performance.application.dtos.PerformanceResponse;
import com.ticketPing.performance.domain.entity.Performance;
import com.ticketPing.performance.domain.repository.PerformanceRepository;
import com.ticketPing.performance.presentation.cases.exception.PerformanceExceptionCase;
import com.ticketPing.performance.presentation.cases.exception.ScheduleExceptionCase;
import common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final PerformanceRepository performanceRepository;

    @Transactional(readOnly = true)
    public PerformanceResponse getPerformance(UUID id) {
        Performance performance = findPerformanceById(id);
        return PerformanceResponse.of(performance);
    }

    @Transactional
    public Page<PerformanceResponse> getAllPerformances(Pageable pageable) {
        Page<Performance> performances = performanceRepository.findAll(pageable);
        return performances.map(PerformanceResponse::of);
    }

    @Transactional
    public Performance getAndValidatePerformance(UUID performanceId) {
        Performance performance = findPerformanceById(performanceId);

        LocalDateTime cur = LocalDateTime.now();
        if(performance.getReservationStartDate().isAfter(cur)
                || performance.getReservationEndDate().isBefore(cur)) {
            throw new ApplicationException(ScheduleExceptionCase.NOT_RESERVATION_DATE);
        }

        return performance;
    }

    @Transactional
    public Performance findPerformanceById(UUID id) {
        return performanceRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(PerformanceExceptionCase.PERFORMANCE_NOT_FOUND));
    }
}


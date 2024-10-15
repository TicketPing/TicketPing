package com.ticketPing.performance.presentation.controller;

import com.ticketPing.performance.application.dtos.PerformanceResponse;
import com.ticketPing.performance.application.dtos.ScheduleResponse;
import com.ticketPing.performance.application.service.PerformanceService;
import com.ticketPing.performance.application.service.ScheduleService;
import com.ticketPing.performance.domain.entity.Performance;
import com.ticketPing.performance.presentation.cases.success.PerformanceSuccessCase;
import common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/performances")
@RequiredArgsConstructor
public class PerformanceController {
    private final PerformanceService performanceService;
    private final ScheduleService scheduleService;

    @Operation(summary = "공연 조회")
    @GetMapping("/{performanceId}")
    public ResponseEntity<CommonResponse<PerformanceResponse>> getPerformance(@PathVariable("performanceId") UUID performanceId) {
        PerformanceResponse performanceResponse = performanceService.getPerformance(performanceId);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(PerformanceSuccessCase.PERFORMANCE_DETAIL_SUCCESS, performanceResponse));
    }

    @Operation(summary = "공연 목록 조회")
    @GetMapping
    public ResponseEntity<CommonResponse<Page<PerformanceResponse>>> getAllPerformances(Pageable pageable) {
        Page<PerformanceResponse> performanceResponses = performanceService.getAllPerformances(pageable);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(PerformanceSuccessCase.PERFORMANCE_LIST_SUCCESS, performanceResponses));
    }

    // TODO: fetch join으로 변경?
    @Operation(summary = "공연 스케줄 목록 조회")
    @GetMapping("/{performanceId}/schedules")
    public ResponseEntity<CommonResponse<Page<ScheduleResponse>>> getSchedulesByPerformance(@PathVariable("performanceId") UUID performanceId, Pageable pageable) {
        Performance performance = performanceService.getAndValidatePerformance(performanceId);
        Page<ScheduleResponse> scheduleResponses = scheduleService.getSchedulesByPerformance(performance, pageable);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(PerformanceSuccessCase.SCHEDULE_LIST_SUCCESS, scheduleResponses));
    }
}


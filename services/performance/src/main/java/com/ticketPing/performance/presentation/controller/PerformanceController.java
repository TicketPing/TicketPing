package com.ticketPing.performance.presentation.controller;

import com.ticketPing.performance.application.dtos.PerformanceResponse;
import com.ticketPing.performance.application.dtos.ScheduleResponse;
import com.ticketPing.performance.application.service.PerformanceService;
import com.ticketPing.performance.application.service.ScheduleService;
import com.ticketPing.performance.application.service.SeatService;
import com.ticketPing.performance.domain.model.entity.Performance;
import com.ticketPing.performance.domain.model.entity.Schedule;
import com.ticketPing.performance.presentation.cases.success.PerformanceSuccessCase;
import com.ticketPing.performance.presentation.cases.success.ScheduleSuccessCase;
import common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/performances")
@RequiredArgsConstructor
public class PerformanceController {
    private final PerformanceService performanceService;
    private final ScheduleService scheduleService;
    private final SeatService seatService;

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

    @Operation(summary = "공연 스케줄 목록 조회")
    @GetMapping("/{performanceId}/schedules")
    public ResponseEntity<CommonResponse<Page<ScheduleResponse>>> getSchedulesByPerformance(@PathVariable("performanceId") UUID performanceId, Pageable pageable) {
        Performance performance = performanceService.getAndValidatePerformance(performanceId);
        Page<ScheduleResponse> scheduleResponses = scheduleService.getSchedulesByPerformance(performance, pageable);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(PerformanceSuccessCase.SCHEDULE_LIST_SUCCESS, scheduleResponses));
    }

    @Operation(summary = "공연 전체 좌석 캐싱 생성")
    @PostMapping("/{performanceId}/seats-cache")
    public ResponseEntity<CommonResponse<Object>> createSeatsCache(@PathVariable("performanceId") UUID performanceId) {
        Performance performance = performanceService.findPerformanceById(performanceId);
        List<Schedule> schedules = scheduleService.finadAllScheduleByPerformance(performance);
        seatService.createSeatsCache(schedules, performanceId);
        return ResponseEntity
                .status(201)
                .body(CommonResponse.success(ScheduleSuccessCase.SCHEDULE_SEATS_CACHED));
    }
}


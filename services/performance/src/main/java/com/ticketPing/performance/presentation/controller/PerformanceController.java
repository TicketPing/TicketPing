package com.ticketPing.performance.presentation.controller;

import com.ticketPing.performance.application.dtos.PerformanceResponseDto;
import com.ticketPing.performance.application.dtos.PerformanceScheduleResponseDto;
import com.ticketPing.performance.application.service.PerformanceService;
import com.ticketPing.performance.presentation.response.success.PerformanceSuccessCase;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import response.CommonResponse;

@RestController
@RequestMapping("/api/v1/performances")
@RequiredArgsConstructor
public class PerformanceController {

    private final PerformanceService performanceService;

    // 공연 단건 조회
    @GetMapping("/{performanceId}")
    public CommonResponse<PerformanceResponseDto> getPerformance(@PathVariable UUID performanceId) {
        PerformanceResponseDto performanceResponseDto = performanceService.getPerformance(
            performanceId);

        return CommonResponse.success(PerformanceSuccessCase.PERFORMANCE_DETAIL_SUCCESS,
            performanceResponseDto);
    }

    // 공연 전체 조회
    @GetMapping
    public CommonResponse<List<PerformanceResponseDto>> getAllPerformances() {
        List<PerformanceResponseDto> performanceResponseDtoList = performanceService.getAllPerformances();

        return CommonResponse.success(PerformanceSuccessCase.PERFORMANCE_LIST_SUCCESS,
            performanceResponseDtoList);
    }


    // 공연 일정 단건 조회 (날짜 기준)
    @GetMapping("/date/{date}")
    public CommonResponse<List<PerformanceScheduleResponseDto>> getSchedulesByDate(
        @PathVariable String date) {
        LocalDate localDate = LocalDate.parse(date); // 문자열을 LocalDate로 변환
        List<PerformanceScheduleResponseDto> performanceScheduleResponseDtoList = performanceService.getAllPerformancesByDate(
            localDate);

        return CommonResponse.success(PerformanceSuccessCase.PERFORMANCE_SCHEDULE_SUCCESS,
            performanceScheduleResponseDtoList);
    }
}


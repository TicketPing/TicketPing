package com.ticketPing.performance.presentation.controller;

import com.ticketPing.performance.application.dtos.PerformanceResponse;
import com.ticketPing.performance.application.service.PerformanceService;
import com.ticketPing.performance.presentation.cases.success.PerformanceSuccessCase;
import common.response.CommonResponse;
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

    @GetMapping("/{performanceId}")
    public ResponseEntity<CommonResponse<PerformanceResponse>> getPerformance(@PathVariable UUID performanceId) {
        PerformanceResponse performanceResponse = performanceService.getPerformance(performanceId);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(PerformanceSuccessCase.PERFORMANCE_DETAIL_SUCCESS, performanceResponse));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<Page<PerformanceResponse>>> getAllPerformances(Pageable pageable) {
        Page<PerformanceResponse> performanceResponses = performanceService.getAllPerformances(pageable);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(PerformanceSuccessCase.PERFORMANCE_DETAIL_SUCCESS, performanceResponses));
    }

//
//    // 공연 일정 단건 조회 (날짜 기준)
//    @GetMapping("/date/{date}")
//    public CommonResponse<List<PerformanceScheduleResponseDto>> getSchedulesByDate(
//        @PathVariable String date) {
//        LocalDate localDate = LocalDate.parse(date); // 문자열을 LocalDate로 변환
//        List<PerformanceScheduleResponseDto> performanceScheduleResponseDtoList = performanceService.getAllPerformancesByDate(
//            localDate);
//
//        return CommonResponse.success(PerformanceSuccessCase.PERFORMANCE_SCHEDULE_SUCCESS,
//            performanceScheduleResponseDtoList);
//    }
//
//    @GetMapping("/hall-seats/{performanceHallId}")
//    public List<OrderPerformanceDto> getHallSeatsByPerformanceHallId(@PathVariable UUID performanceHallId) {
//
//        return performanceService.findByPerformanceHallId(performanceHallId); // 좌석 리스트 반환
//    }

}


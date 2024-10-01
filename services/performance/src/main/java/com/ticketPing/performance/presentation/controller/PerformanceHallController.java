package com.ticketPing.performance.presentation.controller;

import static com.ticketPing.performance.presentation.response.success.PerformanceSuccessCase.PERFORMANCE_HALL_DETAIL_SUCCESS;
import static com.ticketPing.performance.presentation.response.success.PerformanceSuccessCase.PERFORMANCE_HALL_LIST_SUCCESS;

import com.ticketPing.performance.application.dtos.PerformanceHallResponseDto;
import com.ticketPing.performance.application.service.PerformanceHallService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import response.CommonResponse;

@RestController
@RequestMapping("/api/v1/performance-halls")
@RequiredArgsConstructor
public class PerformanceHallController {

    private final PerformanceHallService hallService;

    // 공연장 단건 조회
    @GetMapping("/{performanceHallId}")
    public CommonResponse<PerformanceHallResponseDto> getPerformanceHall(
        @PathVariable UUID performanceHallId) {
        PerformanceHallResponseDto performanceHallResponseDto = hallService.getPerformanceHall(
            performanceHallId);

        return CommonResponse.success(PERFORMANCE_HALL_DETAIL_SUCCESS, performanceHallResponseDto);
    }

    // 공연장 전체 조회
    @GetMapping
    public CommonResponse<List<PerformanceHallResponseDto>> getAllPerformanceHalls() {
        List<PerformanceHallResponseDto> performanceHallResponseDtoList = hallService.getAllPerformanceHalls();

        return CommonResponse.success(PERFORMANCE_HALL_LIST_SUCCESS,
            performanceHallResponseDtoList);
    }

}


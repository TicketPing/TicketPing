package com.ticketPing.performance.presentation.controller;

import com.ticketPing.performance.application.dtos.ScheduleResponse;
import com.ticketPing.performance.application.service.ScheduleService;
import com.ticketPing.performance.presentation.cases.success.ScheduleSuccessCase;
import common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @Operation(summary = "스케줄 조회")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<CommonResponse<ScheduleResponse>> getSchedule(@PathVariable("scheduleId") UUID scheduleId) {
        ScheduleResponse scheduleResponse = scheduleService.getSchedule(scheduleId);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(ScheduleSuccessCase.SCHEDULE_SUCCESS, scheduleResponse));
    }
}

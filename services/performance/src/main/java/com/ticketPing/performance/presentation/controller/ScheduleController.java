package com.ticketPing.performance.presentation.controller;

import com.ticketPing.performance.application.dtos.ScheduleResponse;
import com.ticketPing.performance.application.dtos.SeatResponse;
import com.ticketPing.performance.application.service.ScheduleService;
import com.ticketPing.performance.application.service.SeatService;
import com.ticketPing.performance.domain.model.entity.Schedule;
import com.ticketPing.performance.presentation.cases.success.ScheduleSuccessCase;
import common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final SeatService seatService;

    @Operation(summary = "스케줄 조회")
    @GetMapping("/{scheduleId}")
    public ResponseEntity<CommonResponse<ScheduleResponse>> getSchedule(@PathVariable("scheduleId") UUID scheduleId) {
        ScheduleResponse scheduleResponse = scheduleService.getSchedule(scheduleId);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(ScheduleSuccessCase.SCHEDULE_SUCCESS, scheduleResponse));
    }

    @Operation(summary = "스케줄 전체 좌석 조회")
    @GetMapping("/{scheduleId}/seats")
    public ResponseEntity<CommonResponse<List<SeatResponse>>> getAllScheduleSeats(@PathVariable("scheduleId") UUID scheduleId) {
        List<SeatResponse> seatResponses = seatService.getAllScheduleSeats(scheduleId);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(ScheduleSuccessCase.GET_SCHEDULE_SEATS, seatResponses));
    }
}

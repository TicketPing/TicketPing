package com.ticketPing.performance.presentation.controller;

import com.ticketPing.performance.application.dtos.SeatResponse;
import com.ticketPing.performance.application.service.SeatService;
import com.ticketPing.performance.presentation.cases.success.SeatSuccessCase;
import common.response.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/seat")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/{seatId}")
    public ResponseEntity<CommonResponse<SeatResponse>> getSeat(@PathVariable UUID seatId) {
        SeatResponse seatResponse = seatService.getSeat(seatId);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(SeatSuccessCase.SUCCESS_SEAT, seatResponse));
    }
}

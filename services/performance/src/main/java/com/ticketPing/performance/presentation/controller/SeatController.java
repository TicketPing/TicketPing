package com.ticketPing.performance.presentation.controller;

import com.ticketPing.performance.application.dtos.OrderInfoResponse;
import com.ticketPing.performance.application.dtos.SeatResponse;
import com.ticketPing.performance.application.service.SeatService;
import com.ticketPing.performance.presentation.cases.success.SeatSuccessCase;
import common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/seats")
@RequiredArgsConstructor
public class SeatController {
    private final SeatService seatService;

    @Operation(summary = "좌석 정보 조회")
    @GetMapping("/{seatId}")
    public ResponseEntity<CommonResponse<SeatResponse>> getSeat(@PathVariable("seatId") UUID seatId) {
        SeatResponse seatResponse = seatService.getSeat(seatId);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(SeatSuccessCase.SUCCESS_SEAT, seatResponse));
    }

    @Operation(summary = "좌석 주문 정보 조회 (order 서비스에서 호출용)")
    @GetMapping("/{seatId}/order-info")
    public ResponseEntity<CommonResponse<OrderInfoResponse>> getOrderInfo(@PathVariable("seatId") UUID seatId) {
        OrderInfoResponse orderInfoResponse = seatService.getOrderInfo(seatId);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(SeatSuccessCase.SUCCESS_SEAT_ORDERINFO, orderInfoResponse));
    }

    @Operation(summary = "좌석 상태 수정 (order 서비스에서 호출용)")
    @PutMapping("/{seatId}")
    public ResponseEntity<CommonResponse<SeatResponse>> updateSeatState(@PathVariable("seatId") UUID seatId,
                                                                        @RequestParam("seatState") Boolean seatState) {
        SeatResponse seatResponse = seatService.updateSeatState(seatId, seatState);
        return ResponseEntity
                .status(200)
                .body(CommonResponse.success(SeatSuccessCase.SUCCESS_UPDATE_SEAT_STATE, seatResponse));
    }
}

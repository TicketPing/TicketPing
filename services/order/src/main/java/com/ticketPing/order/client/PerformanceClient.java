package com.ticketPing.order.client;


import com.ticketPing.order.application.dtos.temp.OrderInfoResponse;
import com.ticketPing.order.application.dtos.temp.SeatResponse;
import common.response.CommonResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "performance") // FeignClient 이름과 기본 경로 설정
public interface PerformanceClient extends PerformanceService {

    @GetMapping("/api/v1/seats/{seatId}/order-info")
    ResponseEntity<CommonResponse<OrderInfoResponse>> getOrderInfo(
        @PathVariable("seatId") UUID seatId);

    @PatchMapping("/api/v1/seats/{seatId}")
    ResponseEntity<CommonResponse<SeatResponse>> updateSeatState(
        @PathVariable("seatId") UUID seatId,
        @RequestParam("seatState") Boolean seatState);
}


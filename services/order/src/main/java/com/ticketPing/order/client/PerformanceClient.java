package com.ticketPing.order.client;


import dto.OrderPerformanceDto;
import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "performance") // FeignClient 이름
public interface PerformanceClient extends PerformanceService{

    @GetMapping("/api/v1/performances/hall-seats/{performanceHallId}")
    List<OrderPerformanceDto> getHallSeatsByPerformanceHallId(@PathVariable("performanceHallId") UUID performanceHallId);

    @GetMapping("/api/v1/performance-halls/name/{performanceHallId}")
    String getPerformanceHallName(@PathVariable UUID performanceHallId);
}

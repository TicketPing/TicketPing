package com.ticketPing.order.presentation.controller;

import static com.ticketPing.order.presentation.response.success.OrderSuccessCase.ORDER_SUCCESS;

import com.ticketPing.order.application.dtos.OrderCreateRequestDto;
import com.ticketPing.order.application.dtos.OrderCreateResponseDto;
import com.ticketPing.order.application.service.OrderService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import common.response.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/performance-halls/{performanceHallId}/seats/redis")
    public void savePerformanceOrderToRedis(@PathVariable UUID performanceHallId) {
        orderService.savePerformanceOrderToRedis(performanceHallId);

    }



    @PostMapping
    public CommonResponse<OrderCreateResponseDto> orderPerformanceSeats(
        @RequestBody OrderCreateRequestDto requestDto) {
        OrderCreateResponseDto orderCreateResponseDto = orderService.orderPerformanceSeats(requestDto);

        return CommonResponse.success(ORDER_SUCCESS,orderCreateResponseDto);
    }
}

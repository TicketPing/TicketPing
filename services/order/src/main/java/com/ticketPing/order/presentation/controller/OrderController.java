package com.ticketPing.order.presentation.controller;

import com.ticketPing.order.application.dtos.OrderCreateRequestDto;
import com.ticketPing.order.application.dtos.OrderCreateResponseDto;
import com.ticketPing.order.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import response.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vi")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public CommonResponse<OrderCreateResponseDto> createOrder(@RequestBody OrderCreateRequestDto requestDto) {
        return null;
    }
}

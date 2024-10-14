package com.ticketPing.order.presentation.controller;

import static com.ticketPing.order.presentation.response.success.OrderSuccessCase.ORDER_OCCUPYING_SEAT_SUCCESS;
import static com.ticketPing.order.presentation.response.success.OrderSuccessCase.ORDER_REQUEST_PAYMENT_SUCCESS;
import static com.ticketPing.order.presentation.response.success.OrderSuccessCase.ORDER_SEATS_LIST_RESPONSE;

import com.ticketPing.order.application.dtos.OrderCreateRequestDto;
import com.ticketPing.order.application.dtos.OrderCreateResponseDto;
import com.ticketPing.order.application.service.OrderService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        OrderCreateResponseDto orderCreateResponseDto = orderService.orderOccupyingSeats(requestDto);

        return CommonResponse.success(ORDER_OCCUPYING_SEAT_SUCCESS,orderCreateResponseDto);
    }

    @GetMapping("/seat-list")
    public CommonResponse<List<OrderCreateResponseDto>> orderSeatsResponse() {
        List<OrderCreateResponseDto> orderCreateResponseDtoList = orderService.orderSeatsList();
        return CommonResponse.success(ORDER_SEATS_LIST_RESPONSE,orderCreateResponseDtoList);
    }

    @PostMapping("/payment")
    public CommonResponse<OrderCreateResponseDto> orderRequestPaymentForOccupiedSeats(
        @RequestBody OrderCreateRequestDto requestDto
    ) {
        OrderCreateResponseDto orderCreateResponseDto = orderService.orderRequestPaymentForOccupiedSeats(requestDto);

        return CommonResponse.success(ORDER_REQUEST_PAYMENT_SUCCESS,orderCreateResponseDto);
    }

    @PutMapping("{orderId}/status")
    public void updateOrderStatus( //성공 이냐 실패로 나눔
        @PathVariable("orderId")UUID orderId,
        @RequestParam("status") String status
    ) {
        orderService.updateOrderStatus(orderId,status);
    }

    @PostMapping("/test")
    public void test() {
        orderService.test();
    }

}

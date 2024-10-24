package com.ticketPing.order.presentation.controller;

import com.ticketPing.order.application.dtos.OrderCreateDto;
import com.ticketPing.order.application.dtos.OrderResponse;
import com.ticketPing.order.application.dtos.UserReservationDto;
import com.ticketPing.order.application.service.OrderService;
import common.response.CommonResponse;
import dto.PaymentRequestDto;
import dto.PaymentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import static com.ticketPing.order.presentation.cases.success.OrderSuccessCase.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "예매 좌석 생성 + 좌석 선점")
    @PostMapping
    public CommonResponse<OrderResponse> createOrder(@RequestBody OrderCreateDto requestDto,
                                                     @RequestParam("performanceId") UUID performanceId,
                                                     @RequestHeader("X_USER_ID") UUID userId) {
        OrderResponse orderResponse = orderService.createOrder(requestDto,userId);
        return CommonResponse.success(SUCCESS_CREATE_ORDER, orderResponse);
    }

    @Operation(summary = "주문 정보 조회 (결제 서버용)")
    @GetMapping("/{orderId}/info")
    public PaymentResponseDto getOrderInfo(@PathVariable("orderId") UUID orderId){
        return orderService.orderInfoResponseToPayment(orderId);
    }

    @Operation(summary = "중복 예매 검증 (결제 서버용)")
    @PostMapping("/verify")
    public boolean verifyOrder(@RequestBody PaymentRequestDto requestDto) {
        return orderService.verifyOrder(requestDto);
    }

    @Operation(summary = "예매 상태 변경 (결제 서버용)")
    @PutMapping("{orderId}/status")
    public void updateOrderStatus( @PathVariable("orderId") UUID orderId,
                                   @RequestParam("status") String status,
                                   @RequestParam("performanceId") UUID performanceId) {
        orderService.updateOrderStatus(orderId, status, performanceId);
    }

    @Operation(summary = "사용자 예매 목록 전체 조회")
    @GetMapping("/user/reservations")
    public CommonResponse<List<UserReservationDto>> getUserReservation(@RequestHeader("X_USER_ID") UUID userId) {
        List<UserReservationDto> userReservationDto = orderService.getUserReservation(userId);
        return CommonResponse.success(ORDER_USER_RESERVATION_LIST_RETURN_SUCCESS, userReservationDto);
    }
}

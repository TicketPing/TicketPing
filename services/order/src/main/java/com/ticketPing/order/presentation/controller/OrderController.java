package com.ticketPing.order.presentation.controller;

import static com.ticketPing.order.presentation.cases.success.OrderSuccessCase.ORDER_OCCUPYING_SEAT_SUCCESS;
import static com.ticketPing.order.presentation.cases.success.OrderSuccessCase.ORDER_SEATS_RETURN_SUCCESS;
import static com.ticketPing.order.presentation.cases.success.OrderSuccessCase.ORDER_USER_RESERVATION_LIST_RETURN_SUCCESS;

import com.ticketPing.order.application.dtos.OrderCreateDto;
import com.ticketPing.order.application.dtos.OrderPerformanceDetails;
import com.ticketPing.order.application.dtos.OrderResponse;
import com.ticketPing.order.application.dtos.UserReservationDto;
import com.ticketPing.order.application.service.OrderService;
import common.response.CommonResponse;
import dto.PaymentRequestDto;
import dto.PaymentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "예매 좌석 선점", description = "레디스에서 캐싱된 공연정보를 바탕으로 TTL 좌석 선점 5분간 진행")
    @PostMapping
    public CommonResponse<OrderResponse> orderPerformanceSeats(@RequestBody OrderCreateDto requestDto, @RequestHeader("X_USER_ID") UUID userId
    ) {
        OrderResponse orderResponse = orderService.orderOccupyingSeats(requestDto,userId);
        return CommonResponse.success(ORDER_OCCUPYING_SEAT_SUCCESS, orderResponse);
    }

    @Operation(summary = "결제 성공/실패 후 호출",
        description = "결제가 성공이면 캐싱된 redis 공연정보가 OCCUPIED 로 되고 performanceDB에 "
            + "업데이트합니다. 실패시엔 유지가 됩니다. ")
    @PutMapping("{orderId}/status")
    public void updateOrderStatus( @PathVariable("orderId") UUID orderId, @RequestParam("status") String status) {
        orderService.updateOrderStatus(orderId, status);
    }

    @PostMapping("/test")
    public void test() {
        orderService.test();
    }

    @Operation(summary = "결제 도메인에서 주문 정보를 가져옴",
        description = "주문정보를 가져옵니다."
    )
    @GetMapping("/{orderId}/info")
    public PaymentResponseDto getOrderInfo(@PathVariable("orderId") UUID orderId){
        return orderService.orderInfoResponseToPayment(orderId);
    }

    @GetMapping("/verify")
    public boolean verifyOrder(@RequestBody PaymentRequestDto requestDto) {
        return orderService.verifyOrder(requestDto);
    }

    @GetMapping("/seat-list")
    public CommonResponse<OrderPerformanceDetails> getAllSeats() {
        OrderPerformanceDetails orderPerformanceDetails = orderService.getAllSeats();
        return CommonResponse.success(ORDER_SEATS_RETURN_SUCCESS,orderPerformanceDetails);
    }

    @GetMapping("/user/reservations")
    public CommonResponse<List<UserReservationDto>> getUserReservation(@RequestHeader("X_USER_ID") UUID userId) {
        List<UserReservationDto> userReservationDto = orderService.getUserReservation(userId);
        return CommonResponse.success(ORDER_USER_RESERVATION_LIST_RETURN_SUCCESS, userReservationDto);
    }

}

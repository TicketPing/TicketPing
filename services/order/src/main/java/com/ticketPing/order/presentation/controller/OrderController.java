package com.ticketPing.order.presentation.controller;

import com.ticketPing.order.application.dtos.OrderCreateDto;
import com.ticketPing.order.application.dtos.OrderResponse;
import com.ticketPing.order.application.service.OrderService;
import common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public CommonResponse<OrderResponse> orderPerformanceSeats(
        @RequestBody OrderCreateDto requestDto) {

        return orderService.orderOccupyingSeats(requestDto);
    }

//    @GetMapping("/seat-list")
//    public CommonResponse<List<OrderCreateResponseDto>> orderSeatsResponse() {
//        List<OrderCreateResponseDto> orderCreateResponseDtoList = orderService.orderSeatsList();
//        return CommonResponse.success(ORDER_SEATS_LIST_RESPONSE,orderCreateResponseDtoList);
//    }

//    @PostMapping("/payment")
//    public CommonResponse<Void> orderRequestPaymentForOccupiedSeats(@RequestBody OrderCreateDto requestDto) {
//        orderService.orderRequestPaymentForOccupiedSeats(requestDto);
//        return CommonResponse.success(ORDER_REQUEST_PAYMENT_SUCCESS, null);
//    }

    @Operation(summary = "결제 성공/실패 후 호출",
        description = "결제가 성공이면 캐싱된 redis 공연정보가 OCCUPIED 로 되고 performanceDB에 "
            + "업데이트합니다. 실패시엔 유지가 됩니다. ")
    @PutMapping("{orderId}/status")
    public void updateOrderStatus( //성공 이냐 실패로 나눔
        @PathVariable("orderId") UUID orderId,
        @RequestParam("status") String status
    ) {
        orderService.updateOrderStatus(orderId, status);
    }

    @PostMapping("/test")
    public void test() {
        orderService.test();
    }

}

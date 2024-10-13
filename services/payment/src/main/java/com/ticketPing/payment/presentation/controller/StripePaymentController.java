package com.ticketPing.payment.presentation.controller;

import com.ticketPing.payment.application.dto.StripeCreatePaymentResponse;
import com.ticketPing.payment.application.dto.StripeResponseDto;
import com.ticketPing.payment.application.service.StripePaymentService;
import com.ticketPing.payment.presentation.request.StripeRequestDto;
import common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.ticketPing.payment.cases.PaymentSuccessCase.ORDER_CALL_SUCCESS;
import static com.ticketPing.payment.cases.PaymentSuccessCase.PAYMENT_INTENT_SUCCESS;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class StripePaymentController {

    private final StripePaymentService paymentService;

    @Operation(summary = "결제 요청", description = "실제 결제 전 pi, secretClient 받기 위한 api")
    @PostMapping("/{orderId}")
    public CommonResponse<StripeCreatePaymentResponse> paymentIntent(
            @PathVariable("orderId") UUID orderId
    ) {
        System.out.println("도착");
        // Todo : orderId @RequestParam으로 받아오기
        //UUID orderId = UUID.randomUUID();
        var requestDto = new StripeRequestDto(orderId);
        StripeCreatePaymentResponse responseDto = paymentService.payment(orderId, requestDto);
        //시크릿 키, dpmCheckerLink
        return CommonResponse.success(PAYMENT_INTENT_SUCCESS, responseDto);
    }

    @Operation(summary = "결제 확인 및 상태 변경", description = "결제 완료 여부 확인 api")
    @PatchMapping("/{paymentIntentId}")
    public CommonResponse<StripeResponseDto> updateStatus(@PathVariable("paymentIntentId") String paymentIntentId) {
//        paymentService.updateStatus(paymentIntentId);
        return CommonResponse.success(ORDER_CALL_SUCCESS);
    }


}
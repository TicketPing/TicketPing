package com.ticketPing.payment.presentation.controller;

import com.ticketPing.payment.application.dto.StripeForClientResponseDto;
import com.ticketPing.payment.application.dto.StripeResponseDto;
import com.ticketPing.payment.application.service.StripePaymentService;
import common.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Logger;

import static com.ticketPing.payment.presentation.cases.PaymentSuccessCase.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Slf4j
public class StripePaymentController {

    private static final Logger logger = Logger.getLogger(StripePaymentController.class.getName());

    private final StripePaymentService paymentService;

    @Operation(summary = "결제 요청", description = "실제 결제 전 pi, secretClient 받기 위한 api")
    @PostMapping("/{orderId}")
    public CommonResponse<StripeForClientResponseDto> paymentIntent(@PathVariable("orderId") UUID orderId) {
        StripeForClientResponseDto responseDto = paymentService.payment(orderId);
        //시크릿 키, dpmCheckerLink
        return CommonResponse.success(PAYMENT_INTENT_SUCCESS, responseDto);
    }

    //결제 전 ttl 확인
    @Operation(summary = "결제 전 TTL 확인", description = "결제 전 TTL 확인 api")
    @PostMapping("/verify-ttl/{orderId}")
    public ResponseEntity verifyTtl(@PathVariable("orderId") UUID orderId) {
        paymentService.verifyTtl(orderId);
        return ResponseEntity.status(TTL_VERIFY_SUCCESS.getHttpStatus()).body(CommonResponse.success(TTL_VERIFY_SUCCESS));
    }


    @Operation(summary = "결제 확인 및 상태 변경", description = "결제 완료 여부 확인 api")
    @PatchMapping("/{paymentIntentId}")
    public CommonResponse<StripeResponseDto> updateStatus(@PathVariable("paymentIntentId") String paymentIntentId) {
        paymentService.updateStatus(paymentIntentId);
        return CommonResponse.success(ORDER_CALL_SUCCESS);
    }


}
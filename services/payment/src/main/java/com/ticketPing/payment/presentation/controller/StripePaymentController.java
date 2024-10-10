package com.ticketPing.payment.presentation.controller;

import com.stripe.model.PaymentIntent;
import com.ticketPing.payment.application.dto.StripeResponseDto;
import com.ticketPing.payment.application.service.StripePaymentService;
import com.ticketPing.payment.infrastructure.client.ReservationClient;
import com.ticketPing.payment.presentation.request.StripeRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class StripePaymentController {

    private final StripePaymentService paymentService;

    @Operation(summary = "결제 요청", description = "실제 결제 전 pi, secretClient 받기 위한 api")
    @PostMapping
    public ResponseEntity paymentIntent(@RequestBody StripeRequestDto requestDto) {
        // Todo : orderId @RequestParam으로 받아오기
        UUID orderId = UUID.randomUUID();
        StripeResponseDto responseDto = paymentService.payment(orderId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @Operation(summary = "결제 및 주문(예매) 상태 변경", description = "(임의) 결제 상태 변경 및 주문 상태 변경 위한 주문 호출 api")
    @PatchMapping("/{paymentId}")
    public ResponseEntity updateStatus(@PathVariable("paymentId") UUID paymentId) {
        paymentService.updateStatus(paymentId);
        return ResponseEntity.ok("결제 상태 변경 성공");
    }
}

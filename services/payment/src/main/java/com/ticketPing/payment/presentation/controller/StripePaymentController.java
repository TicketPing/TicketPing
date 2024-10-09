package com.ticketPing.payment.presentation.controller;

import com.stripe.model.PaymentIntent;
import com.ticketPing.payment.application.dto.StripeResponseDto;
import com.ticketPing.payment.application.service.StripePaymentService;
import com.ticketPing.payment.presentation.request.StripeRequestDto;
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

    @PostMapping
    public ResponseEntity paymentIntent(@RequestBody StripeRequestDto requestDto) {
        // Todo : orderId @RequestParam으로 받아오기
        UUID orderId = UUID.randomUUID();
        StripeResponseDto responseDto = paymentService.payment(orderId, requestDto);
        return ResponseEntity.ok(responseDto);
    }
}

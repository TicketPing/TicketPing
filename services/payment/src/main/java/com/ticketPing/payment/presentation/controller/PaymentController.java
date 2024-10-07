package com.ticketPing.payment.presentation.controller;

import com.ticketPing.payment.application.dto.PaymentRequestDto;
import com.ticketPing.payment.application.dto.PaymentResponseDto;
import com.ticketPing.payment.application.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.hibernate.sql.results.spi.SingleResultConsumer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments/toss")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> requestPayments(@RequestBody PaymentRequestDto paymentRequestDto) {
        try {
            PaymentResponseDto paymentResponseDto = paymentService.requestPayments(paymentRequestDto);
            return ResponseEntity.ok(paymentResponseDto);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("requestPayments controller 오류");
        }
    }

//    @GetMapping("/success")
//    public ResponseEntity requestFinalPayments(@RequestParam String paymentKey,
//                                               @RequestParam UUID orderId,
//                                               @RequestParam Long amount
//    ) {
//        try {
//            System.out.println("paymentKey : " + paymentKey);
//            System.out.println("orderId : " + orderId);
//            System.out.println("amount : " + amount);
//
//            paymentService.verifyRequest(paymentKey, orderId, amount);
//            String result = paymentService.requestFinalPayment(paymentKey, orderId, amount);
//
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new IllegalArgumentException("requestFinalPayments 에러");
//        }
//
//    }
}

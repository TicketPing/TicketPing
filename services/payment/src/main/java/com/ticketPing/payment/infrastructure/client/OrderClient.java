package com.ticketPing.payment.infrastructure.client;

import dto.PaymentRequestDto;
import dto.PaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "order", path = "/api/v1/orders")
public interface OrderClient {

    @GetMapping("/{orderId}/info")
    PaymentResponseDto getOrderInfo(@PathVariable("orderId") UUID orderId);

    @PostMapping("/verify")
    boolean verifyOrder(@RequestBody PaymentRequestDto requestDto);


    @PutMapping("/{orderId}/status")
    void updateOrderStatus(@PathVariable("orderId")UUID orderId,
                           @RequestParam("status") String status,
                           @RequestParam("performanceId") UUID performanceId);

//    @GetMapping("/")
//    UUID getSeatId(UUID orderId) {
//        return UUID.randomUUID();
//    };
}

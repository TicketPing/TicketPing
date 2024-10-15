package com.ticketPing.payment.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "order-service")
@RequestMapping("/api/v1/orders")
public interface ReservationClient {



    @PutMapping("/order/{orderId}/status")
    void updateOrderStatus(@PathVariable("orderId")UUID orderId,
                           @RequestParam("status") String status);

//    @GetMapping("/")
//    UUID getSeatId(UUID orderId) {
//        return UUID.randomUUID();
//    };
}

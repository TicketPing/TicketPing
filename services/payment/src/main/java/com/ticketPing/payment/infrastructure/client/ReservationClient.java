package com.ticketPing.payment.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "order-service")
public interface ReservationClient {

    @PutMapping("/order/{orderId}/status")
    void updateOrderStatus(@PathVariable("orderId")UUID orderId,
                           @RequestParam("status") String status);
}

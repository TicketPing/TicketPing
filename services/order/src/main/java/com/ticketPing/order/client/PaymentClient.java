package com.ticketPing.order.client;

import com.ticketPing.order.application.dtos.temp.StripeRequestDto;
import com.ticketPing.order.application.dtos.temp.StripeResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "payment")
public interface PaymentClient {

    @PostMapping("/api/v1/payments")
    StripeResponseDto createPaymentIntent(@RequestBody StripeRequestDto requestDto);
}

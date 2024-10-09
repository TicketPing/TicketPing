package com.ticketPing.payment.application.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
public class StripeResponseDto {
    private String paymentIntentId;
    private UUID orderId;
    private String userEmail;
    private String status;
    private Long amount;
    //private String currency;
    private String clientSecret;
    private String performanceName;
    private String performanceTime;
    private String seatInfo;


    public StripeResponseDto(String paymentIntentId, UUID orderId, String userEmail, String status, Long amount, String clientSecret, String description) {
        this.paymentIntentId = paymentIntentId;
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.status = status;
        this.amount = amount;
        //this.currency = currency;
        this.clientSecret = clientSecret;
        String[] parts = description.split(" , ");
        this.performanceName = parts[0];
        this.performanceTime = parts[1];
        this.seatInfo = parts[2];
    }
}

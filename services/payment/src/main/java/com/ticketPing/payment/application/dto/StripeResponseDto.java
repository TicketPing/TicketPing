package com.ticketPing.payment.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
public class StripeResponseDto {
    @Schema(description = "Stripe paymentIntentId")
    private String paymentIntentId;
    private UUID orderId;
    private String userEmail;
    @Schema(description = "payment status", example = "canceled, processing, requires_action, requires_capture, requires_confirmation," +
            "requires_payment_method,succeeded")
    private String status;
    private Long amount;
    //private String currency;
    @Schema(description = "for frontend key")
    private String clientSecret;
    private String performanceName;
    private String performanceTime;
    private String seatInfo;
    @Schema(description = "payment Intent Time _ unix timestamp", example = "1680800504")
    private Long paymentIntentTime;


    public StripeResponseDto(String paymentIntentId, UUID orderId, String userEmail, String status, Long amount, String clientSecret, String description, Long paymentIntentTime) {
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
        this.paymentIntentTime = paymentIntentTime;
    }
}

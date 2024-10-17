package com.ticketPing.payment.application.dto;

import com.stripe.model.PaymentIntent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
public class StripeResponseDto {
    @Schema(description = "Stripe paymentIntentId")
    private String paymentIntentId;
    private UUID orderId;
    private UUID userId;
    @Schema(description = "payment status", example = "canceled, processing, requires_action, requires_capture, requires_confirmation," +
            "requires_payment_method,succeeded")
    private String status;
    private Long amount;
    @Schema(description = "for frontend key")
    private String clientSecret;
    private String performanceName;
    private UUID performanceScheduleId;
    private String seatInfo;
    @Schema(description = "payment Intent Time _ unix timestamp", example = "1680800504")
    private Long paymentIntentTime;


    public StripeResponseDto(PaymentIntent paymentIntent, UUID orderId) {
        this.paymentIntentId = paymentIntent.getId();
        this.orderId = orderId;
        this.status = paymentIntent.getStatus();
        this.amount = paymentIntent.getAmount();
        this.clientSecret = paymentIntent.getClientSecret();
        String[] parts = paymentIntent.getDescription().split(":");
        this.performanceName = parts[0];
        this.performanceScheduleId = UUID.fromString(parts[1]);
        this.seatInfo = parts[2];
        this.userId = UUID.fromString(parts[3]);
        this.paymentIntentTime = paymentIntent.getCreated();
    }
}

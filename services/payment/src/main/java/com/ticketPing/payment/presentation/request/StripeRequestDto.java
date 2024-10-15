package com.ticketPing.payment.presentation.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class StripeRequestDto {

    private String performanceName;
    @Schema(description = "Time of the performance", example = "2024-12-24T20:00:00")
    private String performanceTime;
    private String seatInfo;
    @Schema(description = "price")
    private Long amount;
    private String userEmail;

    public StripeRequestDto(UUID id) {
        this.performanceName = "test";
        this.performanceTime = "09.08.05:00";
        this.seatInfo = "11";
        this.amount = 10000L;
        this.userEmail = "test@gmail.com";
    }
}

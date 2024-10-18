package com.ticketPing.payment.presentation.request;

import dto.PaymentResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class PaymentStripeRequestDto {

    private String performanceName;
    @Schema(description = "Time of the performance", example = "2024-12-24T20:00:00")
    private UUID performanceScheduleId;
    private UUID seatId;
    @Schema(description = "price")
    private Long amount;
    private UUID userId;

//    public PaymentStripeRequestDto(UUID id) {
//        this.performanceName = "test";
//        this.performanceTime = "09.08.05:00";
//        this.seatInfo = "11";
//        this.amount = 10000L;
//        this.userEmail = "test@gmail.com";
//    }

    public static PaymentStripeRequestDto get(PaymentResponseDto paymentResponseDto) {
        return PaymentStripeRequestDto.builder()
                .performanceName(paymentResponseDto.getPerformanceName())
                .performanceScheduleId(paymentResponseDto.getPerformanceScheduleId())
                .seatId(paymentResponseDto.getSeatId())
                .amount(paymentResponseDto.getAmount())
                .userId(paymentResponseDto.getUserId())
                .build();

    }
}

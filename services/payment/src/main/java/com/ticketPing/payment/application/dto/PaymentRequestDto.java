package com.ticketPing.payment.application.dto;

import com.ticketPing.payment.domain.enums.PayType;
import com.ticketPing.payment.domain.model.Payment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentRequestDto {

    private UUID orderId;
    private PayType payType;
    private Long amount;
    private String performanceName;
    private String companyName;
    private String customerEmail;
    private String customerName;

    public Payment toEntity() {
        return Payment.builder()
                //Todo : orderId 받아오기
                .orderId(orderId)
                .payType(payType)
                .amount(amount)
                .performanceName(performanceName)
                .companyName(companyName)
                .customerEmail(customerEmail)
                .customerName(customerName)
                .paymentTime(LocalDateTime.now())
                .paySuccessYn(true)
                .build();
    }
}

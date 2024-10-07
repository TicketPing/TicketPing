package com.ticketPing.payment.application.dto;

import com.ticketPing.payment.domain.enums.PayType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentResponseDto {
    private PayType payType;
    private Long amount;
    private UUID orderId;
    private String performanceName;
    private String companyName;
    private String customerEmail;
    private String customerName;
    private String successUrl;
    private String failUrl;
    private LocalDateTime paymentTime;
    private boolean paySuccessYn;

}

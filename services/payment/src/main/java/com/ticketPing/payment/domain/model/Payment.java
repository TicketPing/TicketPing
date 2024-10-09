package com.ticketPing.payment.domain.model;

import audit.BaseEntity;
import com.ticketPing.payment.application.dto.PaymentResponseDto;
import com.ticketPing.payment.domain.enums.PayType;
import com.ticketPing.payment.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "P_PAYMENTS")
public class Payment extends BaseEntity {

    @Id
    private UUID paymentId;
    private String companyName;
    private String customerEmail;
    private String customerName;

    @Enumerated(EnumType.STRING)
    private PayType payType;
    @Setter
    private String paymentKey;
    private String performanceName;
    private LocalDateTime paymentTime;
    private long amount;
    private boolean paySuccessYn;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    //mapping
    private UUID orderId;
    //mapping
    private UUID userId;
    //mapping
    //private UUID receiptId;

    public PaymentResponseDto toResponseDto() {
        return PaymentResponseDto.builder()
                .payType(payType)
                .amount(amount)
                .orderId(orderId)
                .performanceName(performanceName)
                .companyName(companyName)
                .customerEmail(customerEmail)
                .customerName(customerName)
                .paymentTime(paymentTime)
                .paySuccessYn(paySuccessYn)
                .build();
    }

    @PrePersist
    protected void createUUID(){
        if(paymentId == null) paymentId = UUID.randomUUID();
    }
}

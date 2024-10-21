package com.ticketPing.payment.domain.model.entity;

import audit.BaseEntity;
import com.ticketPing.payment.application.dto.StripeResponseDto;
import com.ticketPing.payment.domain.model.enums.PayStatus;
import com.ticketPing.payment.domain.model.enums.PayType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "P_PAYMENTS")
public class Payment extends BaseEntity {

    @Id
    private UUID paymentId;
    //Stripe intent id
    private String paymentIntentId;
    //private String currency;
    private UUID userId;
    private String status;
    private String clientSecret;
    @Embedded
    private OrderInfo orderInfo;
    private Long paymentIntentTime;
    private LocalDateTime updateTime;
    @Transient //db 저장 X
    private String previousPaymentStatus;

    @Builder
    public Payment(StripeResponseDto responseDto) {
        this.paymentIntentId = responseDto.getPaymentIntentId();
        this.status = responseDto.getStatus();
        this.clientSecret = responseDto.getClientSecret();
        this.userId = responseDto.getUserId();
        this.orderInfo = new OrderInfo();
        this.orderInfo.setOrderId(responseDto.getOrderId());
        this.orderInfo.setAmount(responseDto.getAmount());
        this.orderInfo.setSeatId(responseDto.getSeatId());
        this.orderInfo.setPerformanceName(responseDto.getPerformanceName());
        this.orderInfo.setPerformanceScheduleId(responseDto.getPerformanceScheduleId());
        this.paymentIntentTime = responseDto.getPaymentIntentTime();
    }

    public void saveStripeStatus(String status) {
        if("succeeded".equals(status)) {
            this.status = PayStatus.SUCCESS.getValue();
        } else {
            this.status = PayStatus.FAIL.getValue();
        }

    }

    @PrePersist
    protected void onPrePersist() {
        if (paymentId == null) paymentId = UUID.randomUUID();
        if (paymentIntentTime != null) {
            Instant instant = Instant.ofEpochSecond(paymentIntentTime);
            this.updateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        }
        this.previousPaymentStatus = this.status;
    }

    // 결제 상태가 변경될 때만 updateTime update
    @PreUpdate
    public void onPreUpdate() {
        if (!this.status.equals(this.previousPaymentStatus)) {
            this.updateTime = LocalDateTime.now();  // 상태가 변경되었을 때만 업데이트 시간 갱신
            this.previousPaymentStatus = this.status;  // 이전 상태를 현재 상태로 갱신
        }
    }

}

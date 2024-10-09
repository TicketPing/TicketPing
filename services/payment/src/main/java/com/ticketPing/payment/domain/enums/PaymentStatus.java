package com.ticketPing.payment.domain.enums;

import lombok.Getter;

@Getter
public enum PaymentStatus {
    //Todo : 결제 대기 추가
    PAYMENT_FAIL("결제 실패"),
    PAYMENT_COMPLETED("결제 완료"),
    PAYMENT_CONFIRMED("결제 확정"),
    REFUND_ING("환불 진행 중"),
    REFUND_COMPLETED("환불 완료");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }
}

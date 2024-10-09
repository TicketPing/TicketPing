package com.ticketPing.payment.application.dto;

import lombok.Getter;

@Getter
public class PaymentResponseHandleCancelDto {
    Long cancelAmount;
    String cancelReason;
    Long taxFreeAmount;
    Long taxExemptionAmount;
    Long refundableAmount;
    Long easyPayDiscountAmount;
    String canceledAt;
    String transactionKey;
    String receiptKey;
    String cancelStatus;
    String cancelRequestId; //비동기 결제에만 적용 & 일반결제, 자동결제, 페이팔 해외결제는 항상 null
}

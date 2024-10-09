package com.ticketPing.payment.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseHandleDto {

    String mId; //상점 Id
    String version; //payment 객체 응답 version
    String type; //결제 타입
    String paymentKey;
    String orderId;
    String orderName;
    String currency;
    String method; //결제 수단
    String totalAmount;
    String balanceAmount;
    String suppliedAmount;
    String vat;
    String status; //결제 상태
    String requestAt;
    String approvedAt;
    String useEscrow;
    String cultureExpense;
    PaymentResponseHandleCardDto cardDto;
    PaymentResponseHandleCancelDto cancelDto;
    //Todo : receiptUrl

}

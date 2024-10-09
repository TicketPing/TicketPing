package com.ticketPing.payment.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponseHandleCardDto {

    String issuerCode; //카드 발급사 두자리 코드
    String acquirerCode; // 카드 매입사 두자리 코드
    String number; //card number
    String installmentPlanMonths; //할부
    String isInterestFree;
    String approveNo;
    String useCardPoint;
    String cardType;
    String ownerType;
    String acquirerStatus;

}

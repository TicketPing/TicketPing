package com.ticketPing.payment.presentation.cases;

import common.cases.SuccessCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentSuccessCase implements SuccessCase {

    PAYMENT_INTENT_SUCCESS(HttpStatus.OK, "결제 요청에 성공하였습니다."),
    ORDER_CALL_SUCCESS(HttpStatus.OK, "주문 호출에 성공하였습니다."),
    TTL_VERIFY_SUCCESS(HttpStatus.OK, "TTL 확인에 성공하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
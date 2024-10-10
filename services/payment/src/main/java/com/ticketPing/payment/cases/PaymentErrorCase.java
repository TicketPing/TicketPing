package com.ticketPing.payment.cases;

import common.cases.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCase implements ErrorCase {

    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 정보가 존재하지 않습니다."),
    PAYMENT_INTENT_FAIL(HttpStatus.BAD_REQUEST, "결제 요청에 실패하였습니다."),
    STATUS_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "상태 변경에 실패하였습니다."),
    FEIGN_CLIENT_FAIL(HttpStatus.BAD_REQUEST, "feignClient 호출 실패");

    private final HttpStatus httpStatus;
    private final String message;
}

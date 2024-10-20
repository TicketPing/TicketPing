package com.ticketPing.payment.presentation.cases;

import common.cases.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PaymentErrorCase implements ErrorCase {

    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 정보가 존재하지 않습니다."),
    PI_CREATE_FAIL(HttpStatus.BAD_REQUEST, "Pi 생성에 실패하였습니다."),
    STATUS_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "상태 변경에 실패하였습니다."),
    FEIGN_CLIENT_FAIL(HttpStatus.BAD_REQUEST, "feignClient 호출 실패"),
    TTL_VERIFY_FAIL(HttpStatus.BAD_REQUEST, "TTL이 만료되었습니다."),
    ORDER_VERIFY_FAIL(HttpStatus.BAD_REQUEST, "이미 예매된 공연 좌석입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

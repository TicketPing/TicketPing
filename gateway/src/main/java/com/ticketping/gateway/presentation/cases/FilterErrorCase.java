package com.ticketping.gateway.presentation.cases;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FilterErrorCase {

    PERFORMANCE_SOLD_OUT(HttpStatus.BAD_REQUEST, "공연이 매진되었습니다."),
    TOO_MANY_WAITING_USERS(HttpStatus.BAD_REQUEST, "잔여 좌석 수에 비해 너무 많은 인원이 대기 중입니다."),
    WORKING_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND ,"작업열에 존재하지 않는 사용자입니다.");

    private final HttpStatus httpStatus;
    private final String message;

}

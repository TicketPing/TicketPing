package com.ticketping.gateway.presentation.cases;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SecurityErrorCase implements ErrorCase{

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "JWT Token이 유효하지 않습니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "JWT Token 만료되었습니다."),
    UNSUPPORTED_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "JWT Token가 지원되지 않습니다."),
    ILLEGAL_CLAIM(HttpStatus.UNAUTHORIZED, "JWT claims가 비어있습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

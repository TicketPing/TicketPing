package com.ticketPing.auth.presentation.cases;

import common.cases.ErrorCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCase implements ErrorCase {
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User not found"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "Expired token"),
    UNSUPPORTED_AUTHENTICATION(HttpStatus.UNAUTHORIZED, "Unsupported authentication"),
    ILLEGAL_CLAIM(HttpStatus.UNAUTHORIZED, "Illegal claim"),
    INVALID_ROLE(HttpStatus.UNAUTHORIZED, "Invalid role"),;

    private final HttpStatus httpStatus;
    private final String message;
}

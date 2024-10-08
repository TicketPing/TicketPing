package com.ticketPing.auth.presentation.status;

import common.cases.ErrorCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCase implements ErrorCase {
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User not found"),
    PASSWORD_NOT_EQUAL(HttpStatus.BAD_REQUEST, "Password not equal"),;

    private final HttpStatus httpStatus;
    private final String message;
}

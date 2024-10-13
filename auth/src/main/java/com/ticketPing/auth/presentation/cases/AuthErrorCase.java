package com.ticketPing.auth.presentation.cases;

import common.cases.ErrorCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthErrorCase implements ErrorCase {
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User not found");

    private final HttpStatus httpStatus;
    private final String message;
}

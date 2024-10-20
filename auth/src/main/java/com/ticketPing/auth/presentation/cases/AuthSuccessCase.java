package com.ticketPing.auth.presentation.cases;

import common.cases.SuccessCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthSuccessCase implements SuccessCase {
    SUCCESS_LOGIN(HttpStatus.OK, "Success login"),
    USER_EXISTS(HttpStatus.OK, "User exists"),;

    private final HttpStatus httpStatus;
    private final String message;
}

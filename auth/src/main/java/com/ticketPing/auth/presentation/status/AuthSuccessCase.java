package com.ticketPing.auth.presentation.status;

import cases.SuccessCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthSuccessCase implements SuccessCase {
    SUCCESS_LOGIN(HttpStatus.OK, "Success login");

    private final HttpStatus httpStatus;
    private final String message;
}

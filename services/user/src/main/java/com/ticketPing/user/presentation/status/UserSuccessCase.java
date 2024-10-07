package com.ticketPing.user.presentation.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import cases.SuccessCase;

@Getter
@AllArgsConstructor
public enum UserSuccessCase implements SuccessCase {
    SUCCESS_CREATE_USER(HttpStatus.CREATED, "User created");

    private final HttpStatus httpStatus;
    private final String message;
}

package com.ticketPing.user.presentation.status;

import cases.ErrorCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorCase implements ErrorCase {
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "Email already exists"),
    INVALID_GENDER(HttpStatus.BAD_REQUEST, "Invalid gender"),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "User not found");

    private final HttpStatus httpStatus;
    private final String message;
}

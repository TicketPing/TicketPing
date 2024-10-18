package com.ticketPing.auth.infrastructure.security;

import com.ticketPing.auth.presentation.cases.AuthErrorCase;
import common.exception.ApplicationException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Role {
    USER("USER"),
    COMPANY("COMPANY");

    private final String value;

    public static Role getRole(final String value) {
        return Arrays.stream(Role.values())
                .filter(t -> t.getValue().equals(value))
                .findAny().orElseThrow(() -> new ApplicationException(AuthErrorCase.INVALID_ROLE));
    }
}

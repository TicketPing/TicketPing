package com.ticketPing.auth.presentation.request;

import com.ticketPing.auth.presentation.cases.UserInfoErrorMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AuthLoginRequest(
        @NotBlank(message = UserInfoErrorMessage.EMAIL_REQUIRED)
        @Pattern(regexp = "^[A-Za-z0-9]+@[A-Za-z0-9]+\\.[A-Za-z]{2,6}",
                message = UserInfoErrorMessage.INVALID_EMAIL)
        String email,

        @NotBlank(message = UserInfoErrorMessage.PASSWORD_REQUIRED)
        String password
) { }

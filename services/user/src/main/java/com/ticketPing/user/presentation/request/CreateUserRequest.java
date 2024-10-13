package com.ticketPing.user.presentation.request;

import com.ticketPing.user.presentation.cases.UserInfoErrorMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record CreateUserRequest(
        @NotBlank(message = UserInfoErrorMessage.EMAIL_REQUIRED)
        @Pattern(regexp = "^[A-Za-z0-9]+@[A-Za-z0-9]+.[A-Za-z]{2,6}",
                message = UserInfoErrorMessage.INVALID_EMAIL)
        String email,

        @NotBlank(message = UserInfoErrorMessage.PASSWORD_REQUIRED)
        String password,

        @NotBlank(message = UserInfoErrorMessage.NICKNAME_REQUIRED)
        String nickname,

        @NotNull(message = UserInfoErrorMessage.BIRTHDAY_REQUIRED)
        LocalDate birthday,

        @NotBlank(message = UserInfoErrorMessage.GENDER_REQUIRED)
        String gender
) { }

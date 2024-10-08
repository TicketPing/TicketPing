package com.ticketPing.order.presentation.response.success;

import cases.SuccessCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum OrderSuccessCase implements SuccessCase {

    ORDER_SUCCESS(HttpStatus.OK, "예매 성공 하었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}


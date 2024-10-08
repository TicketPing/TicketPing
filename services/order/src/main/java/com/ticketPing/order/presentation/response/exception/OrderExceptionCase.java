package com.ticketPing.order.presentation.response.exception;


import cases.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderExceptionCase implements ErrorCase {

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "예매정보를 찾을 수 없습니다."),
    ORDER_ALREADY_EXIST(HttpStatus.CONFLICT, "예매목록이 이미 존재합니다."),
    ORDER_ALREADY_OCCUPIED(HttpStatus.CONFLICT, "예매좌석이 이미 점유되어 있습니다."),
    LOCK_ACQUISITION_INTERRUPTED(HttpStatus.INTERNAL_SERVER_ERROR, "락 획득이 인터럽트되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

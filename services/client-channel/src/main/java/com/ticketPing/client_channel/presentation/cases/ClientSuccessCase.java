package com.ticketPing.client_channel.presentation.cases;

import common.cases.SuccessCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ClientSuccessCase implements SuccessCase {

    GET_WAITING_QUEUE_INFO_SUCCESS(HttpStatus.OK, "대기열 정보 조회 완료.");

    private final HttpStatus httpStatus;
    private final String message;
}

package com.ticketPing.queue_manage.presentaion.cases;

import common.cases.SuccessCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QueueSuccessCase implements SuccessCase {

    ENTER_WAITING_QUEUE_SUCCESS(HttpStatus.OK, "대기열 진입 완료."),
    GET_QUEUE_TOKEN_SUCCESS(HttpStatus.OK, "토큰 조회 완료."),
    GET_WORKING_QUEUE_TOKEN_SUCCESS(HttpStatus.OK, "작업열 토큰 조회 완료");

    private final HttpStatus httpStatus;
    private final String message;
}

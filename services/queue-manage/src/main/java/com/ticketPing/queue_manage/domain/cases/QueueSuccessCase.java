package com.ticketPing.queue_manage.domain.cases;

import cases.SuccessCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum QueueSuccessCase implements SuccessCase {

    ENTER_WAITING_QUEUE_SUCCESS(HttpStatus.OK, "대기열에 입장하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

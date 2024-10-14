package com.ticketPing.performance.presentation.cases.success;

import common.cases.SuccessCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ScheduleSuccessCase implements SuccessCase {
    SCHEDULE_SUCCESS(HttpStatus.OK, "공연 스케줄이 조회되었습니다."),
    SCHEDULE_LIST_SUCCESS(HttpStatus.OK, "공연 스케줄 목록이 조회되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

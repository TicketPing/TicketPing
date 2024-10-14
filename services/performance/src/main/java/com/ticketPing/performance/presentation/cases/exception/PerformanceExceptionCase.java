package com.ticketPing.performance.presentation.cases.exception;

import common.cases.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PerformanceExceptionCase implements ErrorCase {

    PERFORMANCE_NOT_FOUND(HttpStatus.NOT_FOUND, "공연을 찾을 수 없습니다."),
    PERFORMANCE_SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "공연날짜를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

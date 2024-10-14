package com.ticketPing.performance.presentation.cases.success;

import common.cases.SuccessCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SeatSuccessCase implements SuccessCase {
    SUCCESS_SEAT(HttpStatus.OK, "좌석 정보가 조회되었습니다."),
    SUCCESS_UPDATE_SEAT_STATE(HttpStatus.OK, "좌석 상태가 업데이트되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

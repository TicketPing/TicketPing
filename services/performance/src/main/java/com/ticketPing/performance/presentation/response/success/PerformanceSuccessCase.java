package com.ticketPing.performance.presentation.response.success;

import cases.SuccessCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PerformanceSuccessCase implements SuccessCase {

    PERFORMANCE_SUCCESS(HttpStatus.OK, "공연이 조회되었습니다."),
    PERFORMANCE_LIST_SUCCESS(HttpStatus.OK, "공연 목록이 조회되었습니다."), // 공연 전체 조회 성공 메시지
    PERFORMANCE_DETAIL_SUCCESS(HttpStatus.OK, "공연 상세 정보가 조회되었습니다."), // 공연 단건 조회 성공 메시지
    PERFORMANCE_SCHEDULE_SUCCESS(HttpStatus.OK, "날짜를 이용한 공연 목록이 조회되었습니다."),
    PERFORMANCE_HALL_DETAIL_SUCCESS(HttpStatus.OK, "공연장 단건 조회되었습니다."),
    PERFORMANCE_HALL_LIST_SUCCESS(HttpStatus.OK, "공연장 목록이 조회되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}


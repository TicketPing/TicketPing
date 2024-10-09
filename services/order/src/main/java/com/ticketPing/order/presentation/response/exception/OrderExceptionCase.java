package com.ticketPing.order.presentation.response.exception;


import cases.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderExceptionCase implements ErrorCase {

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "예매정보를 찾을 수 없습니다."),
    ORDER_NOT_FOUND_AT_REDIS(HttpStatus.NOT_FOUND, "레디스에서 예매정보를 찾을 수 없습니다."),
    ORDER_ALREADY_EXIST(HttpStatus.CONFLICT, "예매목록이 이미 존재합니다."),
    ORDER_EACH_USER_NOT_MATCHED(HttpStatus.CONFLICT, "DB ID와 Redis ID가 일치하지 않습니다."),
    ORDER_ALREADY_OCCUPIED(HttpStatus.CONFLICT, "예매좌석이 이미 점유되어 있습니다."),
    ORDER_SEATS_NOT_OCCUPIED(HttpStatus.NOT_ACCEPTABLE, "예매가 선점되지 않았습니다."),
    ORDER_STATUS_NOT_PENDING(HttpStatus.NOT_ACCEPTABLE, "예매상태가 PENDING이 아닙니다."),
    LOCK_ACQUISITION_INTERRUPTED(HttpStatus.INTERNAL_SERVER_ERROR, "락 획득이 인터럽트되었습니다."),
    USER_ID_WITH_SEATS_NOT_MATCHED(HttpStatus.UNAUTHORIZED,"선점된 좌석 유저 아이디와 결제 유저 아이디가 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

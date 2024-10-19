package com.ticketPing.order.presentation.cases.exception;


import common.cases.ErrorCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderExceptionCase implements ErrorCase {

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "예매정보를 찾을 수 없습니다."),
    ORDER_OF_USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저의 예매정보를 찾을 수 없습니다."),
    ORDER_NOT_FOUND_AT_REDIS(HttpStatus.NOT_FOUND, "레디스에서 예매정보를 찾을 수 없습니다."),
    ORDER_FOR_PERFORMANCE_CACHE_NOT_FOUND(HttpStatus.NOT_FOUND, "레디스에 공연관련 정보가 캐싱되어 있지 않습니다!"),
    ORDER_ALREADY_OCCUPIED(HttpStatus.CONFLICT, "예매좌석이 이미 점유되어 있습니다."),
    LOCK_ACQUISITION_FAIL(HttpStatus.CONFLICT, "락 획득을 실패하였습니다."),
    TTL_ALREADY_EXISTS(HttpStatus.CONFLICT,"TTL 이 이미 존재합니다."),
    TTL_NOT_FOUND(HttpStatus.NOT_FOUND,"TTL 을 찾을 수 없습니다."),
    NOT_FOUND_SCHEDULE_ID_IN_TTL(HttpStatus.NOT_FOUND,"TTL 정보에서 schedule_id를 찾을 수 없습니다."),
    NOT_FOUND_ORDER_ID_IN_TTL(HttpStatus.NOT_FOUND,"TTL 정보에서 order_id를 찾을 수 없습니다."),
    THE_SEAT_ALREADY_PAID_BY_SOMEONE(HttpStatus.CONFLICT, "누군가 좌석을 점유 및 지불하였습니다."),
    JSON_PROCESSING_EXCEPTION(HttpStatus.UNPROCESSABLE_ENTITY, "Redis 값 역직렬화에 실패했습니다."),
    ORDER_IN_REDIS_NOT_OCCUPIED(HttpStatus.GONE,"캐싱된 저장정보에 주문상태가 false 입니다."),
    REQUEST_ORDER_INFORMATION_BY_PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 도메인에서 요청된 orderId로 order 조회를 실패하였습니다."),
    ORDER_STATUS_UNKNOWN(HttpStatus.CONFLICT,"저장된 enum 상태값을 사용해야 합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

package com.ticketPing.order.presentation.cases.success;

import common.cases.SuccessCase;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum OrderSuccessCase implements SuccessCase {

    SUCCESS_CREATE_ORDER(HttpStatus.OK, "좌석 선점 성공 하었습니다."),
    ORDER_REQUEST_PAYMENT_SUCCESS(HttpStatus.OK, "결제 요청 성공 하었습니다."),
    ORDER_RESPONSE_PAYMENT_SUCCESS(HttpStatus.OK, "결제가 완료 되었습니다."),
    ORDER_SEATS_LIST_RESPONSE(HttpStatus.OK, "좌석리스트를 반환하였습니다."),
    ORDER_INFORMATION_TO_PAYMENT_SUCCESS(HttpStatus.OK, "예매 정보 반환 성공하였습니다."),
    ORDER_SEATS_RETURN_SUCCESS(HttpStatus.OK,"좌석 리턴에 성공하였습니다."),
    ORDER_USER_RESERVATION_LIST_RETURN_SUCCESS(HttpStatus.OK,"유저의 예매리스트를 반환하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}


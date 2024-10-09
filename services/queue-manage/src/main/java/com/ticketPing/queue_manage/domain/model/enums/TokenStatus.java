package com.ticketPing.queue_manage.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TokenStatus {

    WAITING("대기중"),
    WORKING("작업중");

    private final String value;

}

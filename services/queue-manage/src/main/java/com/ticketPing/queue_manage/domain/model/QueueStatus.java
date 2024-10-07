package com.ticketPing.queue_manage.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QueueStatus {

    FAIL("진입 실패"),
    WAITING("대기중"),
    WORKING("작업중"),
    COMPLETED("예매 완료"),
    EXPIRED("만료");

    private final String value;

}

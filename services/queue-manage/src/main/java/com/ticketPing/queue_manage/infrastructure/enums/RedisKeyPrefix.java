package com.ticketPing.queue_manage.infrastructure.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKeyPrefix {

    WAITING_QUEUE("WaitingQueue:"),
    WORKING_QUEUE("WorkingQueue:"),
    TOKEN_VALUE("Token:"),
    LEADER_KEY("LeaderKey:");

    private final String value;

}

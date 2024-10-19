package com.ticketping.gateway.infrastructure.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisKeyPrefix {

    WAITING_QUEUE("WaitingQueue:"),
    AVAILABLE_SEATS("AvailableSeats:"),
    TOKEN_VALUE("Token:");

    private final String value;

}

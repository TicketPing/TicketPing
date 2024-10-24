package com.ticketPing.performance.domain.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SeatRate {
    S("S"),
    A("A"),
    B("B");

    private final String value;
}


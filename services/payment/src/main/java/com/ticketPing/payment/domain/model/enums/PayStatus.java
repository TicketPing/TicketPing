package com.ticketPing.payment.domain.model.enums;

import lombok.Getter;

@Getter
public enum PayStatus {
    SUCCESS("success"),
    FAIL("fail");

    private final String value;

    PayStatus(String value) {
        this.value = value;
    }
}

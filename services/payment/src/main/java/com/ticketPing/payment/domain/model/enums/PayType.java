package com.ticketPing.payment.domain.model.enums;

import lombok.Getter;

@Getter
public enum PayType {
    CARD("CARD"),
    CASH("CASH");

    private final String value;

    PayType(String value) {this.value = value;}
}

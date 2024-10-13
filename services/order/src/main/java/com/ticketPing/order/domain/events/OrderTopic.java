package com.ticketPing.order.domain.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderTopic {

    COMPLETED("order-completed");

    private final String topic;

}

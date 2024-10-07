package com.ticketPing.queue_manage.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum QueueName {

    WAITING_QUEUE("waitingQueue"),
    WORKING_QUEUE("workingQueue");

    private final String value;

}

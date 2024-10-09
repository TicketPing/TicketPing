package com.ticketPing.queue_manage.domain.command.waitingQueue;

import static com.ticketPing.queue_manage.domain.model.enums.QueueName.WAITING_QUEUE;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RetrieveTopWaitingTokensCommand {

    private String queueName;
    private int count;

    public static RetrieveTopWaitingTokensCommand create(long count) {
        return RetrieveTopWaitingTokensCommand.builder()
                .queueName(WAITING_QUEUE.getValue())
                .count((int) count)
                .build();
    }

}

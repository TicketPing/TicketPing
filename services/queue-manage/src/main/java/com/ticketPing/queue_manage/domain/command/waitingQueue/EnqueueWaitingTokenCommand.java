package com.ticketPing.queue_manage.domain.command.waitingQueue;

import static com.ticketPing.queue_manage.domain.model.enums.QueueName.WAITING_QUEUE;

import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class EnqueueWaitingTokenCommand {

    private String queueName;
    private String tokenValue;
    private double score;

    public static EnqueueWaitingTokenCommand create(WaitingQueueToken token) {
        return EnqueueWaitingTokenCommand.builder()
                .queueName(WAITING_QUEUE.getValue())
                .tokenValue(token.getTokenValue())
                .score(System.currentTimeMillis() / 1000.0)
                .build();
    }

}
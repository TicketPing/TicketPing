package com.ticketPing.queue_manage.domain.command.waitingQueue;

import static com.ticketPing.queue_manage.infrastructure.enums.RedisKeyPrefix.WAITING_QUEUE;

import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class InsertWaitingQueueTokenCommand {

    private String tokenValue;
    private String queueName;
    private double score;

    public static InsertWaitingQueueTokenCommand create(WaitingQueueToken token) {
        return InsertWaitingQueueTokenCommand.builder()
                .tokenValue(token.getTokenValue())
                .queueName(WAITING_QUEUE.getValue() + token.getPerformanceId())
                .score(System.currentTimeMillis() / 1000.0)
                .build();
    }

}
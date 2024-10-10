package com.ticketPing.queue_manage.domain.command.waitingQueue;

import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class EnqueueWaitingTokenCommand {

    private String performanceName;
    private String tokenValue;
    private String queueName;
    private double score;

    public static EnqueueWaitingTokenCommand create(WaitingQueueToken token) {
        return EnqueueWaitingTokenCommand.builder()
                .performanceName(token.getPerformanceName())
                .tokenValue(token.getTokenValue())
                .queueName(token.getPerformanceName() + "_Queue")
                .score(System.currentTimeMillis() / 1000.0)
                .build();
    }

}
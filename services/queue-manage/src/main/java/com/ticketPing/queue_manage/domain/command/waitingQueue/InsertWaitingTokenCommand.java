package com.ticketPing.queue_manage.domain.command.waitingQueue;

import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class InsertWaitingTokenCommand {

    private String performanceId;
    private String tokenValue;
    private String queueName;
    private double score;

    public static InsertWaitingTokenCommand create(WaitingQueueToken token) {
        return InsertWaitingTokenCommand.builder()
                .performanceId(token.getPerformanceId())
                .tokenValue(token.getTokenValue())
                .queueName(token.getPerformanceId() + "_Queue")
                .score(System.currentTimeMillis() / 1000.0)
                .build();
    }

}
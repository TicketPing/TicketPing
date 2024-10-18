package com.ticketPing.queue_manage.domain.command.waitingQueue;

import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class FindWaitingTokenCommand {

    private String userId;
    private String performanceId;
    private String tokenValue;
    private String queueName;

    public static FindWaitingTokenCommand create(String userId, String performanceId) {
        return FindWaitingTokenCommand.builder()
                .userId(userId)
                .performanceId(performanceId)
                .tokenValue(generateTokenValue(userId, performanceId))
                .queueName(performanceId + "_Queue")
                .build();
    }

}

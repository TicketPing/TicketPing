package com.ticketPing.queue_manage.domain.command.waitingQueue;

import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;
import static com.ticketPing.queue_manage.infrastructure.enums.RedisKeyPrefix.WAITING_QUEUE;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class FindWaitingQueueTokenCommand {

    private String userId;
    private String performanceId;
    private String tokenValue;
    private String queueName;

    public static FindWaitingQueueTokenCommand create(String userId, String performanceId) {
        return FindWaitingQueueTokenCommand.builder()
                .userId(userId)
                .performanceId(performanceId)
                .tokenValue(generateTokenValue(userId, performanceId))
                .queueName(WAITING_QUEUE.getValue() + performanceId)
                .build();
    }

}

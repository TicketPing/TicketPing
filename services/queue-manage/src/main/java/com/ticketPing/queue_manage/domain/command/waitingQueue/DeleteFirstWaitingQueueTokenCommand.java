package com.ticketPing.queue_manage.domain.command.waitingQueue;

import static com.ticketPing.queue_manage.infrastructure.enums.RedisKeyPrefix.WAITING_QUEUE;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class DeleteFirstWaitingQueueTokenCommand {

    private String performanceId;
    private String queueName;

    public static DeleteFirstWaitingQueueTokenCommand create(String tokenValue) {
        String performanceId = tokenValue.split(":")[1];
        return DeleteFirstWaitingQueueTokenCommand.builder()
                .performanceId(performanceId)
                .queueName(WAITING_QUEUE.getValue() + performanceId)
                .build();
    }

}

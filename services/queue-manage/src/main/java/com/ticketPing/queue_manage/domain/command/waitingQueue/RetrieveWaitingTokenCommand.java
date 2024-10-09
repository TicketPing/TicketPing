package com.ticketPing.queue_manage.domain.command.waitingQueue;

import static com.ticketPing.queue_manage.domain.model.enums.QueueName.WAITING_QUEUE;
import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RetrieveWaitingTokenCommand {

    private String queueName;
    private UUID userId;
    private String tokenValue;

    public static RetrieveWaitingTokenCommand create(UUID userId) {
        return RetrieveWaitingTokenCommand.builder()
                .queueName(WAITING_QUEUE.getValue())
                .userId(userId)
                .tokenValue(generateTokenValue(userId))
                .build();
    }

}

package com.ticketPing.queue_manage.application.command;

import static com.ticketPing.queue_manage.domain.model.QueueName.WAITING_QUEUE;
import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RetrieveTokenCommand {
    private String queueName;
    private UUID userId;
    private String user;

    public static RetrieveTokenCommand create(UUID userId) {
        return RetrieveTokenCommand.builder()
                .queueName(WAITING_QUEUE.getValue())
                .userId(userId)
                .user(generateTokenValue(userId))
                .build();
    }
}

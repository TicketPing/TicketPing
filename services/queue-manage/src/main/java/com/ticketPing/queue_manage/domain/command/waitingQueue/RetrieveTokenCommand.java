package com.ticketPing.queue_manage.domain.command.waitingQueue;

import static com.ticketPing.queue_manage.domain.model.enums.QueueName.WAITING_QUEUE;
import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
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

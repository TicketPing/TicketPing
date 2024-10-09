package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.domain.model.enums.QueueName.WORKING_QUEUE;
import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RetrieveWorkingTokenCommand {

    private String queueName;
    private UUID userId;
    private String tokenValue;

    public static RetrieveWorkingTokenCommand create(UUID userId) {
        return RetrieveWorkingTokenCommand.builder()
                .queueName(WORKING_QUEUE.getValue())
                .userId(userId)
                .tokenValue(generateTokenValue(userId))
                .build();
    }

}

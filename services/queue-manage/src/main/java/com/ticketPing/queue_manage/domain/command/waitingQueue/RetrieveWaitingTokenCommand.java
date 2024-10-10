package com.ticketPing.queue_manage.domain.command.waitingQueue;

import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RetrieveWaitingTokenCommand {

    private UUID userId;
    private String performanceName;
    private String tokenValue;
    private String queueName;

    public static RetrieveWaitingTokenCommand create(UUID userId, String performanceName) {
        return RetrieveWaitingTokenCommand.builder()
                .userId(userId)
                .performanceName(performanceName)
                .tokenValue(generateTokenValue(userId, performanceName))
                .queueName(performanceName + "_Queue")
                .build();
    }

}

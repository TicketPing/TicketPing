package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RetrieveWorkingTokenCommand {

    private UUID userId;
    private String performanceName;
    private String tokenValue;

    public static RetrieveWorkingTokenCommand create(UUID userId, String performanceName) {
        return RetrieveWorkingTokenCommand.builder()
                .userId(userId)
                .performanceName(performanceName)
                .tokenValue(generateTokenValue(userId, performanceName))
                .build();
    }

}

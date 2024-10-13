package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class RetrieveWorkingTokenCommand {

    private String userId;
    private String performanceId;
    private String tokenValue;

    public static RetrieveWorkingTokenCommand create(String userId, String performanceId) {
        return RetrieveWorkingTokenCommand.builder()
                .userId(userId)
                .performanceId(performanceId)
                .tokenValue(generateTokenValue(userId, performanceId))
                .build();
    }

}

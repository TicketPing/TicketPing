package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * tokenValue: 사용자 토큰 값 (작업열 토큰 키)
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class FindWorkingQueueTokenCommand {

    private String userId;
    private String performanceId;
    private String tokenValue;

    public static FindWorkingQueueTokenCommand create(String userId, String performanceId) {
        return FindWorkingQueueTokenCommand.builder()
                .userId(userId)
                .performanceId(performanceId)
                .tokenValue(generateTokenValue(userId, performanceId))
                .build();
    }

}

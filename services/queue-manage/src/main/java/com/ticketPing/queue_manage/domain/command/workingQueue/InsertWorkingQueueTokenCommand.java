package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.infrastructure.enums.RedisKeyPrefix.WORKING_QUEUE;
import static com.ticketPing.queue_manage.infrastructure.utils.ConfigHolder.workingQueueTokenTTL;

import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * tokenValue: 사용자 토큰 값 (작업열 토큰 키)
 * queueName: 작업열 이름 (작업 인원 카운터 키)
 * cacheValue: 작업열 토큰 value
 * ttl: 작업열 토큰 TTL
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class InsertWorkingQueueTokenCommand {

    private String tokenValue;
    private String queueName;
    private String cacheValue;
    private long ttlInMinutes;

    public static InsertWorkingQueueTokenCommand create(WorkingQueueToken token) {
        return InsertWorkingQueueTokenCommand.builder()
                .tokenValue(token.getTokenValue())
                .queueName(WORKING_QUEUE.getValue() + token.getPerformanceId())
                .cacheValue("NA")
                .ttlInMinutes(workingQueueTokenTTL()).build();
    }

}

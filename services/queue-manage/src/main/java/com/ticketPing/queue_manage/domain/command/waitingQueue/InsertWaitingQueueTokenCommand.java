package com.ticketPing.queue_manage.domain.command.waitingQueue;

import static com.ticketPing.queue_manage.domain.utils.QueueTokenValueGenerator.generateTokenValue;
import static com.ticketPing.queue_manage.infrastructure.enums.RedisKeyPrefix.WAITING_QUEUE;
import static com.ticketPing.queue_manage.infrastructure.enums.RedisKeyPrefix.WORKING_QUEUE;
import static com.ticketPing.queue_manage.infrastructure.utils.ConfigHolder.workingQueueMaxSize;
import static com.ticketPing.queue_manage.infrastructure.utils.ConfigHolder.workingQueueTokenTTL;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * tokenValue: 사용자 토큰 값 (대기열 Sorted Set 멤버 or 작업열 토큰 키)
 * waitingQueueName: 대기열 이름 (대기열 Sorted Set 키)
 * score: 대기열 진입 시간 (Sorted Set 스코어)
 * workingQueueName: 작업열 이름 (작업 인원 카운터 키)
 * cacheValue: 작업열 토큰 value
 * ttl: 작업열 토큰 TTL
 * workingQueueMaxSize: 작업열 최대 크기
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class InsertWaitingQueueTokenCommand {

    private String userId;
    private String performanceId;
    private String tokenValue;
    private String waitingQueueName;
    private double score;
    private String workingQueueName;
    private String cacheValue;
    private long ttlInMinutes;
    private int workingQueueMaxSlots;

    public static InsertWaitingQueueTokenCommand create(String userId, String performanceId) {
        return InsertWaitingQueueTokenCommand.builder()
                .userId(userId)
                .performanceId(performanceId)
                .tokenValue(generateTokenValue(userId, performanceId))
                .waitingQueueName(WAITING_QUEUE.getValue() + performanceId)
                .score(System.currentTimeMillis() / 1000.0)
                .workingQueueName(WORKING_QUEUE.getValue() + performanceId)
                .cacheValue("NA")
                .ttlInMinutes(workingQueueTokenTTL())
                .workingQueueMaxSlots(workingQueueMaxSize())
                .build();
    }

}

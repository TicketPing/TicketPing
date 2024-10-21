package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.infrastructure.enums.RedisKeyPrefix.WORKING_QUEUE;

import com.ticketPing.queue_manage.domain.model.enums.DeleteWorkingTokenCase;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

/**
 * tokenValue: 사용자 토큰 값 (작업열 토큰 키)
 * queueName: 작업열 이름 (작업 인원 카운터 키)
 */
@Getter
@Builder(access = AccessLevel.PRIVATE)
public class DeleteWorkingQueueTokenCommand {

    private String tokenValue;
    private String queueName;
    private DeleteWorkingTokenCase deleteCase;

    public static DeleteWorkingQueueTokenCommand create(DeleteWorkingTokenCase deleteCase, String tokenValue) {
        String performanceId = tokenValue.split(":")[1];
        return DeleteWorkingQueueTokenCommand.builder()
                .tokenValue(tokenValue)
                .queueName(WORKING_QUEUE.getValue() + performanceId)
                .deleteCase(deleteCase)
                .build();
    }

}

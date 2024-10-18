package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.infrastructure.enums.RedisKeyPrefix.WORKING_QUEUE;

import com.ticketPing.queue_manage.domain.model.enums.DeleteWorkingTokenCase;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

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

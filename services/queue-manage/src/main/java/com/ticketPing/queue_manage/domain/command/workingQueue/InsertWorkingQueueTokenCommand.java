package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.infrastructure.enums.RedisKeyPrefix.WORKING_QUEUE;
import static com.ticketPing.queue_manage.infrastructure.utils.ConfigHolder.workingQueueTokenTTL;

import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class InsertWorkingQueueTokenCommand {

    private String tokenValue;
    private String queueName;
    private String value;
    private long ttl;

    public static InsertWorkingQueueTokenCommand create(WorkingQueueToken token) {
        return InsertWorkingQueueTokenCommand.builder()
                .tokenValue(token.getTokenValue())
                .queueName(WORKING_QUEUE.getValue() + token.getPerformanceId())
                .value("NA")
                .ttl(workingQueueTokenTTL()).build();
    }

}

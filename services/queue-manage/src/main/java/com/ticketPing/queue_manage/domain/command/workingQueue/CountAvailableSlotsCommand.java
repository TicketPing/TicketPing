package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.infrastructure.enums.RedisKeyPrefix.WORKING_QUEUE;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CountAvailableSlotsCommand {

    private String queueName;

    public static CountAvailableSlotsCommand create(String performanceId) {
        return CountAvailableSlotsCommand.builder()
                .queueName(WORKING_QUEUE.getValue() + performanceId)
                .build();
    }

}

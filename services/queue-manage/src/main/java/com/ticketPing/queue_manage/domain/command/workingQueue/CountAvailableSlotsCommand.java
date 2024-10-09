package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.domain.model.enums.QueueName.WORKING_QUEUE;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class CountAvailableSlotsCommand {

    private String performanceName;
    private String queueName;

    public static CountAvailableSlotsCommand create() {
        return CountAvailableSlotsCommand.builder()
                .performanceName("temp")
                .queueName(WORKING_QUEUE.getValue())
                .build();
    }

}

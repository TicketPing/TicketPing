package com.ticketPing.queue_manage.domain.command.workingQueue;

import static com.ticketPing.queue_manage.domain.model.enums.QueueName.WORKING_QUEUE;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class IncrementCounterCommand {

    private String queueName;
    private long value;

    public static IncrementCounterCommand create(){
        return IncrementCounterCommand.builder()
                .queueName(WORKING_QUEUE.getValue())
                .value(1)
                .build();
    }

}

package com.ticketPing.queue_manage.domain.command.waitingQueue;

import static com.ticketPing.queue_manage.domain.model.enums.QueueName.WAITING_QUEUE;

import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class DequeueCommand {

    private String queueName;
    private String user;

    public static DequeueCommand create(WaitingQueueToken token) {
        return DequeueCommand.builder()
                .queueName(WAITING_QUEUE.getValue())
                .user(token.getTokenValue())
                .build();
    }

}

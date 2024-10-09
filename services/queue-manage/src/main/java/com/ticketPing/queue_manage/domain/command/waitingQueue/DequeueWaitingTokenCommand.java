package com.ticketPing.queue_manage.domain.command.waitingQueue;

import static com.ticketPing.queue_manage.domain.model.enums.QueueName.WAITING_QUEUE;

import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class DequeueWaitingTokenCommand {

    private String queueName;
    private String tokenValue;

    public static DequeueWaitingTokenCommand create(WaitingQueueToken token) {
        return DequeueWaitingTokenCommand.builder()
                .queueName(WAITING_QUEUE.getValue())
                .tokenValue(token.getTokenValue())
                .build();
    }

}

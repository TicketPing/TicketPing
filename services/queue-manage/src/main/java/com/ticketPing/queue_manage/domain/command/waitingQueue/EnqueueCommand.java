package com.ticketPing.queue_manage.domain.command.waitingQueue;

import static com.ticketPing.queue_manage.domain.model.enums.QueueName.WAITING_QUEUE;

import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(access = AccessLevel.PRIVATE)
public class EnqueueCommand {

    private String queueName;
    private String user;
    private double score;

    public static EnqueueCommand create(WaitingQueueToken token) {
        return EnqueueCommand.builder()
                .queueName(WAITING_QUEUE.getValue())
                .user(token.getTokenValue())
                .score(System.currentTimeMillis() / 1000.0)
                .build();
    }

}
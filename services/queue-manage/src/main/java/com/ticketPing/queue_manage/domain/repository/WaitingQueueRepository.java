package com.ticketPing.queue_manage.domain.repository;

import com.ticketPing.queue_manage.domain.command.waitingQueue.DeleteFirstWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.FindWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.InsertWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.QueueToken;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import reactor.core.publisher.Mono;

public interface WaitingQueueRepository {
    Mono<QueueToken> insertWaitingQueueToken(InsertWaitingQueueTokenCommand command);
    Mono<WaitingQueueToken> findWaitingQueueToken(FindWaitingQueueTokenCommand command);
    Mono<WaitingQueueToken> deleteFirstWaitingQueueToken(DeleteFirstWaitingQueueTokenCommand command);
}

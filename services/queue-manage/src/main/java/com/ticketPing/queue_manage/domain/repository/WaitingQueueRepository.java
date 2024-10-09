package com.ticketPing.queue_manage.domain.repository;

import com.ticketPing.queue_manage.domain.command.waitingQueue.EnqueueWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.DequeueFirstWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.RetrieveWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import java.util.Optional;

public interface WaitingQueueRepository {
    boolean enqueueWaitingToken(EnqueueWaitingTokenCommand command);
    Optional<WaitingQueueToken> retrieveWaitingToken(RetrieveWaitingTokenCommand command);
    Optional<WaitingQueueToken> dequeueFirstWaitingToken(DequeueFirstWaitingTokenCommand command);
}

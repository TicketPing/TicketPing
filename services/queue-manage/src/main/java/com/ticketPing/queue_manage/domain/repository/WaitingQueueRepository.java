package com.ticketPing.queue_manage.domain.repository;

import com.ticketPing.queue_manage.domain.command.waitingQueue.DequeueWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.EnqueueWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.RetrieveWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.RetrieveTopWaitingTokensCommand;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import java.util.List;
import java.util.Optional;

public interface WaitingQueueRepository {
    boolean enqueueWaitingToken(EnqueueWaitingTokenCommand command);
    Optional<WaitingQueueToken> retrieveWaitingToken(RetrieveWaitingTokenCommand command);
    List<WaitingQueueToken> retrieveTopWaitingTokens(RetrieveTopWaitingTokensCommand command);
    boolean dequeueWaitingToken(DequeueWaitingTokenCommand command);
}

package com.ticketPing.queue_manage.domain.repository;

import com.ticketPing.queue_manage.domain.command.waitingQueue.DequeueCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.EnqueueCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.RetrieveTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.RetrieveTopTokensCommand;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import java.util.List;

public interface WaitingQueueRepository {
    boolean enqueue(EnqueueCommand command);
    WaitingQueueToken retrieveToken(RetrieveTokenCommand command);
    List<WaitingQueueToken> retrieveTopTokens(RetrieveTopTokensCommand command);
    boolean dequeue(DequeueCommand command);
}

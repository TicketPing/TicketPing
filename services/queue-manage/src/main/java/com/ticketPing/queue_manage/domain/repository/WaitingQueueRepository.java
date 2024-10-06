package com.ticketPing.queue_manage.domain.repository;

import com.ticketPing.queue_manage.application.command.EnqueueCommand;
import com.ticketPing.queue_manage.application.command.RetrieveTokenCommand;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;

public interface WaitingQueueRepository {
    boolean enqueue(EnqueueCommand command);
    WaitingQueueToken retrieveToken(RetrieveTokenCommand command);
}

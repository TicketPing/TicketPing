package com.ticketPing.queue_manage.domain.repository;

import com.ticketPing.queue_manage.domain.command.waitingQueue.InsertWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.DeleteFirstWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.FindWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import java.util.Optional;

public interface WaitingQueueRepository {
    boolean insertWaitingQueueToken(InsertWaitingQueueTokenCommand command);
    Optional<WaitingQueueToken> findWaitingQueueToken(FindWaitingQueueTokenCommand command);
    Optional<WaitingQueueToken> deleteFirstWaitingQueueToken(DeleteFirstWaitingQueueTokenCommand command);
}

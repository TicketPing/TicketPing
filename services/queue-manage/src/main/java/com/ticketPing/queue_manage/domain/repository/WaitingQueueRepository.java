package com.ticketPing.queue_manage.domain.repository;

import com.ticketPing.queue_manage.domain.command.waitingQueue.InsertWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.DeleteFirstWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.FindWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import java.util.Optional;

public interface WaitingQueueRepository {
    boolean insertWaitingToken(InsertWaitingTokenCommand command);
    Optional<WaitingQueueToken> findWaitingToken(FindWaitingTokenCommand command);
    Optional<WaitingQueueToken> deleteFirstWaitingToken(DeleteFirstWaitingTokenCommand command);
}

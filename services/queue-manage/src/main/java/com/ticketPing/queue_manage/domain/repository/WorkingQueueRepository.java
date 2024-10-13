package com.ticketPing.queue_manage.domain.repository;

import com.ticketPing.queue_manage.domain.command.workingQueue.DequeueWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.EnqueueWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.CountAvailableSlotsCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.RetrieveWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.model.AvailableSlots;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import java.util.Optional;

public interface WorkingQueueRepository {
    AvailableSlots countAvailableSlots(CountAvailableSlotsCommand command);
    void enqueueWorkingToken(EnqueueWorkingTokenCommand command);
    Optional<WorkingQueueToken> retrieveWorkingToken(RetrieveWorkingTokenCommand command);
    void dequeueWorkingToken(DequeueWorkingTokenCommand command);
}

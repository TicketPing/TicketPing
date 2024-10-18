package com.ticketPing.queue_manage.domain.repository;

import com.ticketPing.queue_manage.domain.command.workingQueue.DeleteWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.InsertWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.CountAvailableSlotsCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.FindWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.model.AvailableSlots;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import java.util.Optional;

public interface WorkingQueueRepository {
    AvailableSlots countAvailableSlots(CountAvailableSlotsCommand command);
    void insertWorkingToken(InsertWorkingTokenCommand command);
    Optional<WorkingQueueToken> findWorkingToken(FindWorkingTokenCommand command);
    void deleteWorkingToken(DeleteWorkingTokenCommand command);
}

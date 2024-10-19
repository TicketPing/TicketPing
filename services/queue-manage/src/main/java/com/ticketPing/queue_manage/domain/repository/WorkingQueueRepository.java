package com.ticketPing.queue_manage.domain.repository;

import com.ticketPing.queue_manage.domain.command.workingQueue.DeleteWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.InsertWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.CountAvailableSlotsCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.FindWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.AvailableSlots;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import java.util.Optional;

public interface WorkingQueueRepository {
    AvailableSlots countAvailableSlots(CountAvailableSlotsCommand command);
    void insertWorkingQueueToken(InsertWorkingQueueTokenCommand command);
    Optional<WorkingQueueToken> findWorkingQueueToken(FindWorkingQueueTokenCommand command);
    void deleteWorkingQueueToken(DeleteWorkingQueueTokenCommand command);
}

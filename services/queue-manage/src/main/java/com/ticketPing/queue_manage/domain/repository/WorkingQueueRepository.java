package com.ticketPing.queue_manage.domain.repository;

import com.ticketPing.queue_manage.domain.command.workingQueue.CacheTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.CountAvailableSlotsCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.IncrementCounterCommand;
import com.ticketPing.queue_manage.domain.model.AvailableSlots;

public interface WorkingQueueRepository {
    AvailableSlots countAvailableSlots(CountAvailableSlotsCommand command);
    void enqueue(CacheTokenCommand cacheTokenCommand, IncrementCounterCommand incrementCounterCommand);
}

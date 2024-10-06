package com.ticketPing.queue_manage.domain.repository;

import com.ticketPing.queue_manage.application.command.EnqueueCommand;

public interface WaitingQueueRepository {
    boolean enqueue(EnqueueCommand command);
}

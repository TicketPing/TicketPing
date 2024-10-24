package com.ticketPing.queue_manage.domain.repository;

import com.ticketPing.queue_manage.domain.command.workingQueue.DeleteWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.InsertWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.FindWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import reactor.core.publisher.Mono;

public interface WorkingQueueRepository {
    Mono<Boolean> insertWorkingQueueToken(InsertWorkingQueueTokenCommand command);
    Mono<WorkingQueueToken> findWorkingQueueToken(FindWorkingQueueTokenCommand command);
    Mono<Boolean> deleteWorkingQueueToken(DeleteWorkingQueueTokenCommand command);
}

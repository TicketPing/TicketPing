package com.ticketPing.queue_manage.domain.service;

import com.ticketPing.queue_manage.domain.command.workingQueue.CacheTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.CountAvailableSlotsCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.DequeueCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.RetrieveTopTokensCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.IncrementCounterCommand;
import com.ticketPing.queue_manage.domain.model.AvailableSlots;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import com.ticketPing.queue_manage.domain.repository.WorkingQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkingQueueDomainServiceImpl implements WorkingQueueDomainService {

    private final WaitingQueueRepository waitingQueueRepository;
    private final WorkingQueueRepository workingQueueRepository;

    @Override
    public void processQueueTransfer() {
        // 작업열 인원 여유 확인
        AvailableSlots availableSlots = workingQueueRepository.countAvailableSlots(CountAvailableSlotsCommand.create());
        if (availableSlots.isLimited()) {
            return;
        }

        // 여유 있으면 대기열 -> 작업열 이동
        waitingQueueRepository.retrieveTopTokens(RetrieveTopTokensCommand.create(availableSlots.getCount())).stream()
                .toList()
                .forEach(waitingToken -> {
                    WorkingQueueToken workingQueueToken = waitingToken.toWorkingQueueToken();
                    workingQueueRepository.enqueue(CacheTokenCommand.create(workingQueueToken), IncrementCounterCommand.create());
                    waitingQueueRepository.dequeue(DequeueCommand.create(waitingToken));
                });
    }

}

package com.ticketPing.queue_manage.application.service;

import static com.ticketPing.queue_manage.presentaion.cases.QueueErrorCase.INVALID_TOKEN;

import com.ticketPing.queue_manage.application.dto.GeneralTokenResponse;
import com.ticketPing.queue_manage.domain.command.waitingQueue.DequeueWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.RetrieveWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.RetrieveTopWaitingTokensCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.CacheWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.CountAvailableSlotsCommand;
import com.ticketPing.queue_manage.domain.model.AvailableSlots;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import com.ticketPing.queue_manage.domain.repository.WorkingQueueRepository;
import common.exception.ApplicationException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkingQueueApplicationServiceImpl implements WorkingQueueApplicationService {

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
        long count = availableSlots.getCount();
        waitingQueueRepository.retrieveTopWaitingTokens(RetrieveTopWaitingTokensCommand.create(count)).stream()
                .toList()
                .forEach(waitingToken -> {
                    WorkingQueueToken workingQueueToken = waitingToken.toWorkingQueueToken();
                    workingQueueRepository.enqueueWorkingToken(CacheWorkingTokenCommand.create(workingQueueToken));
                    waitingQueueRepository.dequeueWaitingToken(DequeueWaitingTokenCommand.create(waitingToken));
                });
    }

    @Override
    public GeneralTokenResponse getWorkingQueueToken(UUID userId) {
        Optional<WorkingQueueToken> token = workingQueueRepository.retrieveWorkingToken(RetrieveWorkingTokenCommand.create(userId));
        return GeneralTokenResponse.from(token.orElseThrow(() -> new ApplicationException(INVALID_TOKEN)));
    }

}

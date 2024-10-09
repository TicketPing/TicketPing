package com.ticketPing.queue_manage.application.service;

import static com.ticketPing.queue_manage.domain.model.WaitingQueueToken.create;
import static com.ticketPing.queue_manage.presentaion.cases.QueueErrorCase.USER_NOT_FOUND;

import com.ticketPing.queue_manage.application.dto.EnterWaitingQueueResponse;
import com.ticketPing.queue_manage.application.dto.GeneralTokenResponse;
import com.ticketPing.queue_manage.domain.command.waitingQueue.EnqueueWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.RetrieveWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.RetrieveWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import com.ticketPing.queue_manage.domain.repository.WorkingQueueRepository;
import common.exception.ApplicationException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueApplicationServiceImpl implements WaitingQueueApplicationService {

    private final WaitingQueueRepository waitingQueueRepository;
    private final WorkingQueueRepository workingQueueRepository;

    @Override
    public EnterWaitingQueueResponse enterWaitingQueue(UUID userId) {
        WaitingQueueToken token = create(userId);
        waitingQueueRepository.enqueueWaitingToken(EnqueueWaitingTokenCommand.create(token));
        return EnterWaitingQueueResponse.from(token);
    }

    @Override
    public GeneralTokenResponse getQueueToken(UUID userId) {
        return waitingQueueRepository.retrieveWaitingToken(RetrieveWaitingTokenCommand.create(userId))
                .map(GeneralTokenResponse::from)
                // 대기열 존재하지 않을 떄
                .orElseGet(() ->
                        workingQueueRepository.retrieveWorkingToken(RetrieveWorkingTokenCommand.create(userId))
                                .map(GeneralTokenResponse::from)
                                // 작업열 존재하지 않을 때
                                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND))
                );
    }

}

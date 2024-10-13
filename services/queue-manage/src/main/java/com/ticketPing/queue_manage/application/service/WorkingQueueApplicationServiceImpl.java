package com.ticketPing.queue_manage.application.service;

import static com.ticketPing.queue_manage.presentaion.cases.QueueErrorCase.INVALID_TOKEN;

import com.ticketPing.queue_manage.application.dto.GeneralTokenResponse;
import com.ticketPing.queue_manage.domain.command.waitingQueue.DequeueFirstWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.DequeueWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.EnqueueWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.RetrieveWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import com.ticketPing.queue_manage.domain.repository.WorkingQueueRepository;
import common.exception.ApplicationException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkingQueueApplicationServiceImpl implements WorkingQueueApplicationService {

    private final WorkingQueueRepository workingQueueRepository;
    private final WaitingQueueRepository waitingQueueRepository;

    @Override
    public GeneralTokenResponse getWorkingQueueToken(String userId, String performanceId) {
        Optional<WorkingQueueToken> token = workingQueueRepository.retrieveWorkingToken(RetrieveWorkingTokenCommand.create(userId, performanceId));
        return GeneralTokenResponse.from(token.orElseThrow(() -> new ApplicationException(INVALID_TOKEN)));
    }

    @Override
    public void processQueueTransfer(String expiredTokenValue) {
        // 작업열 토큰 삭제 및 카운터 감소
        workingQueueRepository.dequeueWorkingToken(DequeueWorkingTokenCommand.create(expiredTokenValue));
        // 대기열 첫번째 토큰 조회 및 삭제
        Optional<WaitingQueueToken> deletedWaitingToken = waitingQueueRepository.dequeueFirstWaitingToken(DequeueFirstWaitingTokenCommand.create(expiredTokenValue));
        if (deletedWaitingToken.isEmpty()) {
            return;
        }
        // 작업열 진입 및 카운터 증가
        WorkingQueueToken workingToken = deletedWaitingToken.get().toWorkingQueueToken();
        workingQueueRepository.enqueueWorkingToken(EnqueueWorkingTokenCommand.create(workingToken));
    }

}

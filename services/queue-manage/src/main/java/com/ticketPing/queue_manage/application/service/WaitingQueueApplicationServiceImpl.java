package com.ticketPing.queue_manage.application.service;

import static com.ticketPing.queue_manage.presentaion.cases.QueueErrorCase.USER_NOT_FOUND;

import com.ticketPing.queue_manage.application.dto.GeneralTokenResponse;
import com.ticketPing.queue_manage.domain.command.waitingQueue.EnqueueWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.RetrieveWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.EnqueueWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.CountAvailableSlotsCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.RetrieveWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.model.AvailableSlots;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import com.ticketPing.queue_manage.domain.repository.WorkingQueueRepository;
import common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueApplicationServiceImpl implements WaitingQueueApplicationService {

    private final WaitingQueueRepository waitingQueueRepository;
    private final WorkingQueueRepository workingQueueRepository;

    @Override
    public GeneralTokenResponse enterWaitingQueue(String userId, String performanceId) {
        // 작업열 인원 여유 확인
        AvailableSlots availableSlots = workingQueueRepository.countAvailableSlots(CountAvailableSlotsCommand.create(performanceId));
        if (availableSlots.isLimited()) {
            return getWaitingTokenResponse(userId, performanceId);
        }
        return getWorkingTokenResponse(userId, performanceId);
    }

    // 대기열 진입
    private GeneralTokenResponse getWaitingTokenResponse(String userId, String performanceId) {
        WaitingQueueToken token = WaitingQueueToken.create(userId, performanceId);
        waitingQueueRepository.enqueueWaitingToken(EnqueueWaitingTokenCommand.create(token));
        return GeneralTokenResponse.from(token);
    }

    // 작업열 진입 및 카운터 증가
    private GeneralTokenResponse getWorkingTokenResponse(String userId, String performanceId) {
        WorkingQueueToken token = WorkingQueueToken.create(userId, performanceId);
        workingQueueRepository.enqueueWorkingToken(EnqueueWorkingTokenCommand.create(token));
        return GeneralTokenResponse.from(token);
    }

    @Override
    public GeneralTokenResponse getQueueToken(String userId, String performanceId) {
        return waitingQueueRepository.retrieveWaitingToken(RetrieveWaitingTokenCommand.create(userId, performanceId))
                .map(GeneralTokenResponse::from)
                // 대기열 존재하지 않을 떄
                .orElseGet(() ->
                        workingQueueRepository.retrieveWorkingToken(RetrieveWorkingTokenCommand.create(userId, performanceId))
                                .map(GeneralTokenResponse::from)
                                // 작업열 존재하지 않을 때
                                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND))
                );
    }

}

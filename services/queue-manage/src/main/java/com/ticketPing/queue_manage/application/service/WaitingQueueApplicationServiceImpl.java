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
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WaitingQueueApplicationServiceImpl implements WaitingQueueApplicationService {

    private final WaitingQueueRepository waitingQueueRepository;
    private final WorkingQueueRepository workingQueueRepository;

    @Override
    public GeneralTokenResponse enterWaitingQueue(UUID userId, String performanceName) {
        AvailableSlots availableSlots = workingQueueRepository.countAvailableSlots(CountAvailableSlotsCommand.create(performanceName));
        // 작업열 인원 여유 확인
        if (availableSlots.isLimited()) {
            return getWaitingTokenResponse(userId, performanceName);
        }
        return getWorkingTokenResponse(userId, performanceName);
    }

    // 대기열 진입
    private GeneralTokenResponse getWaitingTokenResponse(UUID userId, String performanceName) {
        WaitingQueueToken token = WaitingQueueToken.create(userId, performanceName);
        waitingQueueRepository.enqueueWaitingToken(EnqueueWaitingTokenCommand.create(token));
        return GeneralTokenResponse.from(token);
    }

    // 작업열 진입
    private GeneralTokenResponse getWorkingTokenResponse(UUID userId, String performanceName) {
        WorkingQueueToken token = WorkingQueueToken.create(userId, performanceName);
        workingQueueRepository.enqueueWorkingToken(EnqueueWorkingTokenCommand.create(token));
        return GeneralTokenResponse.from(token);
    }

    @Override
    public GeneralTokenResponse getQueueToken(UUID userId, String performanceName) {
        return waitingQueueRepository.retrieveWaitingToken(RetrieveWaitingTokenCommand.create(userId, performanceName))
                .map(GeneralTokenResponse::from)
                // 대기열 존재하지 않을 떄
                .orElseGet(() ->
                        workingQueueRepository.retrieveWorkingToken(RetrieveWorkingTokenCommand.create(userId, performanceName))
                                .map(GeneralTokenResponse::from)
                                // 작업열 존재하지 않을 때
                                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND))
                );
    }

}

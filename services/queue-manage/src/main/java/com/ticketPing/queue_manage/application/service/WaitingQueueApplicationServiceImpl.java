package com.ticketPing.queue_manage.application.service;

import static com.ticketPing.queue_manage.presentaion.cases.QueueErrorCase.USER_NOT_FOUND;

import com.ticketPing.queue_manage.application.dto.GeneralTokenResponse;
import com.ticketPing.queue_manage.domain.command.waitingQueue.InsertWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.FindWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.InsertWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.CountAvailableSlotsCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.FindWorkingTokenCommand;
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
        waitingQueueRepository.insertWaitingToken(InsertWaitingTokenCommand.create(token));
        return GeneralTokenResponse.from(token);
    }

    // 작업열 진입 및 카운터 증가
    private GeneralTokenResponse getWorkingTokenResponse(String userId, String performanceId) {
        WorkingQueueToken token = WorkingQueueToken.create(userId, performanceId);
        workingQueueRepository.insertWorkingToken(InsertWorkingTokenCommand.create(token));
        return GeneralTokenResponse.from(token);
    }

    @Override
    public GeneralTokenResponse getQueueToken(String userId, String performanceId) {
        return waitingQueueRepository.findWaitingToken(FindWaitingTokenCommand.create(userId, performanceId))
                .map(GeneralTokenResponse::from)
                // 대기열 존재하지 않을 떄
                .orElseGet(() ->
                        workingQueueRepository.findWorkingToken(FindWorkingTokenCommand.create(userId, performanceId))
                                .map(GeneralTokenResponse::from)
                                // 작업열 존재하지 않을 때
                                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND))
                );
    }

}

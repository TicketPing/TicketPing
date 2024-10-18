package com.ticketPing.queue_manage.application.service;

import static com.ticketPing.queue_manage.presentaion.cases.QueueErrorCase.USER_NOT_FOUND;

import com.ticketPing.queue_manage.application.dto.GeneralQueueTokenResponse;
import com.ticketPing.queue_manage.domain.command.waitingQueue.InsertWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.FindWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.InsertWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.CountAvailableSlotsCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.FindWorkingQueueTokenCommand;
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
public class WaitingQueueServiceImpl implements WaitingQueueService {

    private final WaitingQueueRepository waitingQueueRepository;
    private final WorkingQueueRepository workingQueueRepository;

    @Override
    public GeneralQueueTokenResponse enterWaitingQueue(String userId, String performanceId) {
        // 작업열 인원 여유 확인
        AvailableSlots availableSlots = workingQueueRepository.countAvailableSlots(CountAvailableSlotsCommand.create(performanceId));
        if (availableSlots.isLimited()) {
            return getWaitingTokenResponse(userId, performanceId);
        }
        return getWorkingTokenResponse(userId, performanceId);
    }

    // 대기열 진입
    private GeneralQueueTokenResponse getWaitingTokenResponse(String userId, String performanceId) {
        WaitingQueueToken token = WaitingQueueToken.create(userId, performanceId);
        waitingQueueRepository.insertWaitingQueueToken(InsertWaitingQueueTokenCommand.create(token));
        return GeneralQueueTokenResponse.from(token);
    }

    // 작업열 진입 및 카운터 증가
    private GeneralQueueTokenResponse getWorkingTokenResponse(String userId, String performanceId) {
        WorkingQueueToken token = WorkingQueueToken.create(userId, performanceId);
        workingQueueRepository.insertWorkingQueueToken(InsertWorkingQueueTokenCommand.create(token));
        return GeneralQueueTokenResponse.from(token);
    }

    @Override
    public GeneralQueueTokenResponse getQueueInfo(String userId, String performanceId) {
        return waitingQueueRepository.findWaitingQueueToken(FindWaitingQueueTokenCommand.create(userId, performanceId))
                .map(GeneralQueueTokenResponse::from)
                // 대기열에 존재하지 않을 때
                .orElseGet(() ->
                        workingQueueRepository.findWorkingQueueToken(FindWorkingQueueTokenCommand.create(userId, performanceId))
                                .map(GeneralQueueTokenResponse::from)
                                // 작업열에 존재하지 않을 때
                                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND))
                );
    }

}

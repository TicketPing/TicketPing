package com.ticketPing.queue_manage.application.service;

import static com.ticketPing.queue_manage.presentaion.cases.QueueErrorCase.USER_NOT_FOUND;

import com.ticketPing.queue_manage.application.dto.GeneralQueueTokenResponse;
import com.ticketPing.queue_manage.domain.command.waitingQueue.FindWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.InsertWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.FindWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.QueueToken;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import com.ticketPing.queue_manage.domain.repository.WorkingQueueRepository;
import common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WaitingQueueServiceImpl implements WaitingQueueService {

    private final WaitingQueueRepository waitingQueueRepository;
    private final WorkingQueueRepository workingQueueRepository;

    @Override
    public GeneralQueueTokenResponse enterWaitingQueue(String userId, String performanceId) {
        QueueToken token = waitingQueueRepository.insertWaitingQueueToken(
                InsertWaitingQueueTokenCommand.create(userId, performanceId));
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

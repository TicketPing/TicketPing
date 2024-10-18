package com.ticketPing.queue_manage.application.service;

import static com.ticketPing.queue_manage.presentaion.cases.QueueErrorCase.WORKING_TOKEN_NOT_FOUND;

import com.ticketPing.queue_manage.application.dto.GeneralQueueTokenResponse;
import com.ticketPing.queue_manage.domain.command.waitingQueue.DeleteFirstWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.DeleteWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.InsertWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.FindWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import com.ticketPing.queue_manage.domain.model.enums.DeleteWorkingTokenCase;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import com.ticketPing.queue_manage.domain.repository.WorkingQueueRepository;
import common.exception.ApplicationException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkingQueueServiceImpl implements WorkingQueueService {

    private final WorkingQueueRepository workingQueueRepository;
    private final WaitingQueueRepository waitingQueueRepository;

    @Override
    public GeneralQueueTokenResponse getWorkingQueueToken(String userId, String performanceId) {
        Optional<WorkingQueueToken> token = workingQueueRepository.findWorkingQueueToken(
                FindWorkingQueueTokenCommand.create(userId, performanceId));
        return GeneralQueueTokenResponse.from(token.orElseThrow(() -> new ApplicationException(WORKING_TOKEN_NOT_FOUND)));
    }

    @Override
    public void processQueueTransfer(DeleteWorkingTokenCase deleteCase, String tokenValue) {
        // 작업열 토큰 삭제 및 카운터 감소
        workingQueueRepository.deleteWorkingQueueToken(DeleteWorkingQueueTokenCommand.create(deleteCase, tokenValue));
        // 대기열 첫번째 토큰 조회 및 삭제
        waitingQueueRepository.deleteFirstWaitingQueueToken(DeleteFirstWaitingQueueTokenCommand.create(tokenValue))
                // 작업열 진입 및 카운터 증가
                .ifPresent(deletedWaitingToken -> workingQueueRepository.insertWorkingQueueToken(
                        InsertWorkingQueueTokenCommand.create(deletedWaitingToken.toWorkingQueueToken())));
    }

}

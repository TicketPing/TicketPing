package com.ticketPing.queue_manage.application.service;

import static com.ticketPing.queue_manage.presentaion.cases.QueueErrorCase.INVALID_TOKEN;

import com.ticketPing.queue_manage.application.dto.GeneralTokenResponse;
import com.ticketPing.queue_manage.domain.command.waitingQueue.DeleteFirstWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.DeleteWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.InsertWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.FindWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
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
public class WorkingQueueApplicationServiceImpl implements WorkingQueueApplicationService {

    private final WorkingQueueRepository workingQueueRepository;
    private final WaitingQueueRepository waitingQueueRepository;

    @Override
    public GeneralTokenResponse getWorkingQueueToken(String userId, String performanceId) {
        Optional<WorkingQueueToken> token = workingQueueRepository.findWorkingToken(
                FindWorkingTokenCommand.create(userId, performanceId));
        return GeneralTokenResponse.from(token.orElseThrow(() -> new ApplicationException(INVALID_TOKEN)));
    }

    @Override
    public void processQueueTransfer(DeleteWorkingTokenCase deleteCase, String expiredTokenValue) {
        // 작업열 토큰 삭제 및 카운터 감소
        workingQueueRepository.deleteWorkingToken(DeleteWorkingTokenCommand.create(deleteCase, expiredTokenValue));
        // 대기열 첫번째 토큰 조회 및 삭제
        Optional<WaitingQueueToken> deletedWaitingToken = waitingQueueRepository.deleteFirstWaitingToken(
                DeleteFirstWaitingTokenCommand.create(expiredTokenValue));
        if (deletedWaitingToken.isEmpty()) {
            return;
        }
        // 작업열 진입 및 카운터 증가
        WorkingQueueToken workingToken = deletedWaitingToken.get().toWorkingQueueToken();
        workingQueueRepository.insertWorkingToken(InsertWorkingTokenCommand.create(workingToken));
    }

}

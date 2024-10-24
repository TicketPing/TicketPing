package com.ticketPing.queue_manage.application.service;

import static com.ticketPing.queue_manage.presentaion.cases.QueueErrorCase.WORKING_TOKEN_NOT_FOUND;

import com.ticketPing.queue_manage.application.dto.GeneralQueueTokenResponse;
import com.ticketPing.queue_manage.domain.command.waitingQueue.DeleteFirstWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.DeleteWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.InsertWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.FindWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.enums.DeleteWorkingTokenCase;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import com.ticketPing.queue_manage.domain.repository.WorkingQueueRepository;
import common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkingQueueService {

    private final WorkingQueueRepository workingQueueRepository;
    private final WaitingQueueRepository waitingQueueRepository;

    public Mono<GeneralQueueTokenResponse> getWorkingQueueToken(String userId, String performanceId) {
        FindWorkingQueueTokenCommand command = FindWorkingQueueTokenCommand.create(userId, performanceId);

        return workingQueueRepository.findWorkingQueueToken(command)
                .doOnSuccess(token -> log.info("작업열 토큰 조회 완료 {}", token))
                .map(token -> GeneralQueueTokenResponse.from(token))
                .switchIfEmpty(Mono.error(new ApplicationException(WORKING_TOKEN_NOT_FOUND)));
    }

    public Mono<Void> processQueueTransfer(DeleteWorkingTokenCase deleteCase, String tokenValue) {
        DeleteWorkingQueueTokenCommand command = DeleteWorkingQueueTokenCommand.create(deleteCase, tokenValue);

        return workingQueueRepository.deleteWorkingQueueToken(command)
                .doOnSuccess(isWorkingQueueTokenDeleted -> log.info("작업열 토큰 삭제 완료 {}", isWorkingQueueTokenDeleted))
                .filter(isWorkingQueueTokenDeleted -> isWorkingQueueTokenDeleted)
                .flatMap(isWorkingQueueTokenDeleted ->
                        waitingQueueRepository.deleteFirstWaitingQueueToken(DeleteFirstWaitingQueueTokenCommand.create(tokenValue))
                                .doOnSuccess(deletedWaitingToken -> log.info("대기열 첫 번째 토큰 삭제 완료 {}", deletedWaitingToken))
                                .flatMap(deletedWaitingToken ->
                                        workingQueueRepository.insertWorkingQueueToken(InsertWorkingQueueTokenCommand.create(deletedWaitingToken.toWorkingQueueToken()))
                                                .doOnSuccess(isWorkingQueueTokenSaved -> log.info("작업열 토큰 저장 완료 {}", isWorkingQueueTokenSaved))
                                )
                )
                .then();
    }

}

package com.ticketPing.queue_manage.infrastructure.repository;

import com.ticketPing.queue_manage.domain.command.waitingQueue.DeleteFirstWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.FindWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.InsertWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.QueueToken;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import com.ticketPing.queue_manage.infrastructure.repository.script.InsertWaitingQueueTokenScript;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {

    private final RedisRepository redisRepository;
    private final InsertWaitingQueueTokenScript script;

    @Override
    public Mono<QueueToken> insertWaitingQueueToken(InsertWaitingQueueTokenCommand command) {
        return script.checkAvailableSlots(command)
                .map(hasSlots -> hasSlots
                        ? WorkingQueueToken.create(command.getUserId(), command.getPerformanceId())
                        : WaitingQueueToken.create(command.getUserId(), command.getPerformanceId())
                );
    }

    @Override
    public Mono<WaitingQueueToken> findWaitingQueueToken(FindWaitingQueueTokenCommand command) {
        return Mono.zip(
                        redisRepository.getMemberRankFromSortedSet(command.getQueueName(), command.getTokenValue()),
                        redisRepository.getSortedSetSize(command.getQueueName())
                )
                .map(tuple -> WaitingQueueToken.withPosition(
                        command.getUserId(),
                        command.getPerformanceId(),
                        command.getTokenValue(),
                        tuple.getT1() + 1,
                        tuple.getT2()
                ));
    }

    @Override
    public Mono<WaitingQueueToken> deleteFirstWaitingQueueToken(DeleteFirstWaitingQueueTokenCommand command) {
        return redisRepository.getFirstMemberFromSortedSet(command.getQueueName())
                .flatMap(tokenValue ->
                        redisRepository.deleteMemberFromSortedSet(command.getQueueName(), tokenValue)
                                .then(Mono.just(tokenValue))
                )
                .map(tokenValue -> WaitingQueueToken.valueOf(command.getPerformanceId(), tokenValue));
    }

}

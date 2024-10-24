package com.ticketPing.queue_manage.infrastructure.repository;

import static com.ticketPing.queue_manage.infrastructure.utils.TTLConverter.toLocalDateTime;

import com.ticketPing.queue_manage.domain.command.workingQueue.DeleteWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.InsertWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.FindWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import com.ticketPing.queue_manage.domain.repository.WorkingQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Slf4j
@Repository
@RequiredArgsConstructor
public class WorkingQueueRepositoryImpl implements WorkingQueueRepository {

    private final RedisRepository redisRepository;

    @Override
    public Mono<Boolean> insertWorkingQueueToken(InsertWorkingQueueTokenCommand command) {
        return redisRepository.getElementFromBucket(command.getTokenValue())
                .hasElement()
                .filter(exists -> !exists)
                .flatMap(__ -> insertToken(command)
                        .then(Mono.just(true))
                )
                .switchIfEmpty(Mono.just(false));
    }

    private Mono<Void> insertToken(InsertWorkingQueueTokenCommand command) {
        return redisRepository.setElementWithTTL(command.getTokenValue(), command.getCacheValue(), command.getTtlInMinutes())
                .then(redisRepository.incrementCounter(command.getQueueName()))
                .then();
    }

    @Override
    public Mono<WorkingQueueToken> findWorkingQueueToken(FindWorkingQueueTokenCommand command) {
        return redisRepository.getElementRemainingTTL(command.getTokenValue())
                .flatMap(ttl -> WorkingQueueToken.withValidUntil(
                        command.getUserId(),
                        command.getPerformanceId(),
                        command.getTokenValue(),
                        toLocalDateTime(ttl)
                ));
    }

    @Override
    public Mono<Boolean> deleteWorkingQueueToken(DeleteWorkingQueueTokenCommand command) {
        return switch (command.getDeleteCase()) {
            case TOKEN_EXPIRED -> handleTokenExpired(command.getQueueName());
            case ORDER_COMPLETED -> handleOrderCompleted(command.getQueueName(), command.getTokenValue());
        };
    }

    private Mono<Boolean> handleTokenExpired(String queueName) {
        return redisRepository.decrementCounter(queueName)
                .then(Mono.just(true));
    }

    private Mono<Boolean> handleOrderCompleted(String queueName, String tokenValue) {
        return redisRepository.getElementFromBucket(tokenValue)
                .hasElement()
                .filter(exists -> exists)
                .flatMap(__ -> deleteToken(queueName, tokenValue)
                        .then(Mono.just(true))
                )
                .switchIfEmpty(Mono.just(false));
    }

    private Mono<Void> deleteToken(String queueName, String tokenValue) {
        return redisRepository.deleteElementFromBucket(tokenValue)
                .then(redisRepository.decrementCounter(queueName))
                .then();
    }

}
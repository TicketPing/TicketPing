package com.ticketPing.queue_manage.domain.repository;

import static com.ticketPing.queue_manage.infrastructure.utils.TTLConverter.toLocalDateTime;

import com.ticketPing.queue_manage.domain.command.workingQueue.DeleteWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.InsertWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.CountAvailableSlotsCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.FindWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.model.AvailableSlots;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkingQueueRepositoryImpl implements WorkingQueueRepository {

    private final RedissonClient redissonClient;

    @Override
    public AvailableSlots countAvailableSlots(CountAvailableSlotsCommand command) {
        RAtomicLong counter = redissonClient.getAtomicLong(command.getPerformanceId());
        return AvailableSlots.from(counter.get());
    }

    @Override
    public void insertWorkingToken(InsertWorkingTokenCommand command) {
        if (!isExistToken(command.getTokenValue())) {
            cacheToken(command);
            increaseCounter(command);
        }
    }

    private boolean isExistToken(String tokenValue) {
        return redissonClient.getBucket(tokenValue).get() != null;
    }

    private void cacheToken(InsertWorkingTokenCommand command) {
        RBucket<String> bucket = redissonClient.getBucket(command.getTokenValue());
        bucket.set(command.getValue(), command.getTtl(), TimeUnit.MINUTES);
    }

    private void increaseCounter(InsertWorkingTokenCommand command) {
        redissonClient.getAtomicLong(command.getPerformanceId()).incrementAndGet();
    }

    @Override
    public Optional<WorkingQueueToken> findWorkingToken(FindWorkingTokenCommand command) {
        RBucket<String> bucket = redissonClient.getBucket(command.getTokenValue());
        String value = bucket.get();
        if (value == null) {
            return Optional.empty();
        }
        long ttl = bucket.remainTimeToLive();
        WorkingQueueToken token = WorkingQueueToken.withValidUntil(command.getUserId(), command.getPerformanceId(), command.getTokenValue(), toLocalDateTime(ttl));
        return Optional.of(token);
    }

    @Override
    public void deleteWorkingToken(DeleteWorkingTokenCommand command) {
        switch (command.getDeleteCase()) {
            case TokenExpired:
                handleTokenExpired(command);
                break;
            case OrderCompleted:
                handleOrderCompleted(command);
                break;
        }
    }

    private void handleTokenExpired(DeleteWorkingTokenCommand command) {
        decreaseCounter(command);
    }

    private void handleOrderCompleted(DeleteWorkingTokenCommand command) {
        if (isExistToken(command.getTokenValue())) {
            deleteToken(command);
            decreaseCounter(command);
        }
    }

    private void deleteToken(DeleteWorkingTokenCommand command) {
        redissonClient.getBucket(command.getTokenValue()).delete();
    }

    private void decreaseCounter(DeleteWorkingTokenCommand command) {
        redissonClient.getAtomicLong(command.getPerformanceId()).decrementAndGet();
    }

}

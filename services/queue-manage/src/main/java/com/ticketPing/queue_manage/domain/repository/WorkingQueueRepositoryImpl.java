package com.ticketPing.queue_manage.domain.repository;

import static com.ticketPing.queue_manage.domain.model.WorkingQueueToken.tokenWithValidUntil;
import static com.ticketPing.queue_manage.infrastructure.utils.TTLConverter.toLocalDateTime;

import com.ticketPing.queue_manage.domain.command.workingQueue.DequeueWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.EnqueueWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.CountAvailableSlotsCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.RetrieveWorkingTokenCommand;
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
    public void enqueueWorkingToken(EnqueueWorkingTokenCommand command) {
        if (!isExistToken(command.getTokenValue())) {
            cacheToken(command);
            increaseCounter(command);
        }
    }

    private boolean isExistToken(String tokenValue) {
        return redissonClient.getBucket(tokenValue).get() != null;
    }

    private void cacheToken(EnqueueWorkingTokenCommand command) {
        RBucket<String> bucket = redissonClient.getBucket(command.getTokenValue());
        bucket.set(command.getValue(), command.getTtl(), TimeUnit.MINUTES);
    }

    private void increaseCounter(EnqueueWorkingTokenCommand command) {
        redissonClient.getAtomicLong(command.getPerformanceId()).incrementAndGet();
    }

    @Override
    public Optional<WorkingQueueToken> retrieveWorkingToken(RetrieveWorkingTokenCommand command) {
        RBucket<String> bucket = redissonClient.getBucket(command.getTokenValue());
        String value = bucket.get();
        if (value == null) {
            return Optional.empty();
        }
        long ttl = bucket.remainTimeToLive();
        WorkingQueueToken token = tokenWithValidUntil(command.getUserId(), command.getPerformanceId(), command.getTokenValue(), toLocalDateTime(ttl));
        return Optional.of(token);
    }

    @Override
    public void dequeueWorkingToken(DequeueWorkingTokenCommand command) {
        if (isExistToken(command.getTokenValue())) {
            deleteToken(command);
            decreaseCounter(command);
        }
    }

    private void deleteToken(DequeueWorkingTokenCommand command) {
        redissonClient.getBucket(command.getTokenValue()).delete();
    }

    private void decreaseCounter(DequeueWorkingTokenCommand command) {
        redissonClient.getAtomicLong(command.getPerformanceId()).decrementAndGet();
    }

}

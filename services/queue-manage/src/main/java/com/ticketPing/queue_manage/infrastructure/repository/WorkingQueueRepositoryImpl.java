package com.ticketPing.queue_manage.infrastructure.repository;

import static com.ticketPing.queue_manage.infrastructure.utils.TTLConverter.toLocalDateTime;

import com.ticketPing.queue_manage.domain.command.workingQueue.DeleteWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.InsertWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.CountAvailableSlotsCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.FindWorkingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.AvailableSlots;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import com.ticketPing.queue_manage.domain.repository.WorkingQueueRepository;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkingQueueRepositoryImpl implements WorkingQueueRepository {

    private final RedisRepository redisRepository;

    @Override
    public AvailableSlots countAvailableSlots(CountAvailableSlotsCommand command) {
        RAtomicLong counter = redisRepository.getAtomicLong(command.getQueueName());
        return AvailableSlots.from(counter.get());
    }

    @Override
    public void insertWorkingQueueToken(InsertWorkingQueueTokenCommand command) {
        if (!isExistToken(command.getTokenValue())) {
            cacheToken(command.getTokenValue(), command.getValue(), command.getTtl());
            increaseCounter(command.getQueueName());
        }
    }

    private boolean isExistToken(String tokenValue) {
        return redisRepository.getBucket(tokenValue).get() != null;
    }

    private void cacheToken(String tokenValue, String value, long ttl) {
        RBucket<String> bucket = redisRepository.getBucket(tokenValue);
        bucket.set(value, ttl, TimeUnit.MINUTES);
    }

    private void increaseCounter(String queueName) {
        redisRepository.getAtomicLong(queueName).incrementAndGet();
    }

    @Override
    public Optional<WorkingQueueToken> findWorkingQueueToken(FindWorkingQueueTokenCommand command) {
        RBucket<String> bucket = redisRepository.getBucket(command.getTokenValue());
        if (bucket.get() == null) {
            return Optional.empty();
        }
        long ttl = bucket.remainTimeToLive();
        WorkingQueueToken token = WorkingQueueToken.withValidUntil(command.getUserId(), command.getPerformanceId(), command.getTokenValue(), toLocalDateTime(ttl));
        return Optional.of(token);
    }

    @Override
    public void deleteWorkingQueueToken(DeleteWorkingQueueTokenCommand command) {
        switch (command.getDeleteCase()) {
            case TOKEN_EXPIRED:
                handleTokenExpired(command.getQueueName());
                break;
            case ORDER_COMPLETED:
                handleOrderCompleted(command.getTokenValue(), command.getQueueName());
                break;
        }
    }

    private void handleTokenExpired(String queueName) {
        decreaseCounter(queueName);
    }

    private void handleOrderCompleted(String tokenValue, String queueName) {
        if (isExistToken(tokenValue)) {
            deleteToken(tokenValue);
            decreaseCounter(queueName);
        }
    }

    private void deleteToken(String tokenValue) {
        redisRepository.getBucket(tokenValue).delete();
    }

    private void decreaseCounter(String queueName) {
        redisRepository.getAtomicLong(queueName).decrementAndGet();
    }

}

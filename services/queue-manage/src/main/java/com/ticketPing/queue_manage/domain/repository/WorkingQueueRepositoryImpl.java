package com.ticketPing.queue_manage.domain.repository;

import static com.ticketPing.queue_manage.domain.model.WorkingQueueToken.tokenWithValidUntil;
import static com.ticketPing.queue_manage.infrastructure.utils.TTLConverter.toLocalDateTime;

import com.ticketPing.queue_manage.domain.command.workingQueue.CacheWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.CountAvailableSlotsCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.RetrieveWorkingTokenCommand;
import com.ticketPing.queue_manage.domain.model.AvailableSlots;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WorkingQueueRepositoryImpl implements WorkingQueueRepository {

    private final RedissonClient redissonClient;

    @Override
    public AvailableSlots countAvailableSlots(CountAvailableSlotsCommand command) {
        RKeys keys = redissonClient.getKeys();
        String pattern = command.getPerformanceName() + "*";
        long count = StreamSupport.stream(keys.getKeysByPattern(pattern).spliterator(), false).count();
        return AvailableSlots.from(count);
    }

    @Override
    public void enqueueWorkingToken(CacheWorkingTokenCommand command) {
        RBucket<String> bucket = redissonClient.getBucket(command.getTokenValue());
        bucket.set(command.getValue(), command.getTtl(), TimeUnit.MINUTES);
    }

    @Override
    public Optional<WorkingQueueToken> retrieveWorkingToken(RetrieveWorkingTokenCommand command) {
        RBucket<String> bucket = redissonClient.getBucket(command.getTokenValue());
        String value = bucket.get();
        if (value == null) {
            return Optional.empty();
        }
        long ttl = bucket.remainTimeToLive();
        WorkingQueueToken token = tokenWithValidUntil(command.getUserId(), command.getTokenValue(), toLocalDateTime(ttl));
        return Optional.of(token);
    }

}

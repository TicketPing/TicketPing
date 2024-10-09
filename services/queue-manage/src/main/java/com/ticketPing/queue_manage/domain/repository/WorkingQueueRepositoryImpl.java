package com.ticketPing.queue_manage.domain.repository;

import static com.ticketPing.queue_manage.presentaion.cases.QueueErrorCase.INVALID_TOKEN;

import com.ticketPing.queue_manage.domain.command.workingQueue.CacheTokenCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.CountAvailableSlotsCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.IncrementCounterCommand;
import com.ticketPing.queue_manage.domain.command.workingQueue.RetrieveTokenCommand;
import com.ticketPing.queue_manage.domain.model.AvailableSlots;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import common.exception.ApplicationException;
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
        RAtomicLong counter = redissonClient.getAtomicLong(command.getQueueName());
        return AvailableSlots.from(counter.get());
    }

    @Override
    public void enqueue(CacheTokenCommand cacheTokenCommand, IncrementCounterCommand incrementCounterCommand) {
        cacheToken(cacheTokenCommand);
        incrementCounter(incrementCounterCommand);
    }

    private boolean cacheToken(CacheTokenCommand command) {
        RBucket<String> bucket = redissonClient.getBucket(command.getUser());
        bucket.set(command.getValue(), command.getTtl(), TimeUnit.MINUTES);
        return true;
    }

    private long incrementCounter(IncrementCounterCommand command) {
        RAtomicLong counter = redissonClient.getAtomicLong(command.getQueueName());
        return counter.addAndGet(command.getValue());
    }

    @Override
    public WorkingQueueToken retrieveToken(RetrieveTokenCommand command) {
        RBucket<String> bucket = redissonClient.getBucket(command.getUser());
        String value = bucket.get();
        if (value == null) {
            throw new ApplicationException(INVALID_TOKEN);
        }
        return WorkingQueueToken.valueOf(command.getUserId(), command.getUser());
    }

}

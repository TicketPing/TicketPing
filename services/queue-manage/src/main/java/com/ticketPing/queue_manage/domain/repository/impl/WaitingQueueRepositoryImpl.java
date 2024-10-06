package com.ticketPing.queue_manage.domain.repository.impl;

import com.ticketPing.queue_manage.application.command.EnqueueCommand;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {

    private final RedissonClient redissonClient;

    @Override
    public boolean enqueue(EnqueueCommand command) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(command.getQueueName());
        return sortedSet.add(command.getScore(), command.getUser());
    }

}

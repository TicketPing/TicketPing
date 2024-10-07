package com.ticketPing.queue_manage.domain.repository.impl;

import static com.ticketPing.queue_manage.domain.cases.QueueErrorCase.USER_NOT_FOUND;
import static com.ticketPing.queue_manage.domain.model.WaitingQueueToken.tokenWithPosition;

import com.ticketPing.queue_manage.application.command.EnqueueCommand;
import com.ticketPing.queue_manage.application.command.RetrieveTokenCommand;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import exception.ApplicationException;
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

    @Override
    public WaitingQueueToken retrieveToken(RetrieveTokenCommand command) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(command.getQueueName());
        int totalSize = sortedSet.size();
        try {
            int position = sortedSet.rank(command.getUser()) + 1;
            return tokenWithPosition(command.getUserId(), command.getUser(), position, totalSize);
        } catch (Exception e) {
            throw new ApplicationException(USER_NOT_FOUND);
        }
    }

}

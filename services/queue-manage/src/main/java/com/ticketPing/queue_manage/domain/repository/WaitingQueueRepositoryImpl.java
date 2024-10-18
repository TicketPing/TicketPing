package com.ticketPing.queue_manage.domain.repository;

import com.ticketPing.queue_manage.domain.command.waitingQueue.InsertWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.DeleteFirstWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.FindWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {

    private final RedissonClient redissonClient;

    @Override
    public boolean insertWaitingToken(InsertWaitingTokenCommand command) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(command.getQueueName());
        return sortedSet.add(command.getScore(), command.getTokenValue());
    }

    @Override
    public Optional<WaitingQueueToken> findWaitingToken(FindWaitingTokenCommand command) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(command.getQueueName());
        try {
            int position = sortedSet.rank(command.getTokenValue()) + 1;
            int totalSize = sortedSet.size();
            WaitingQueueToken token = WaitingQueueToken.withPosition(command.getUserId(), command.getPerformanceId(), command.getTokenValue(), position, totalSize);
            return Optional.of(token);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<WaitingQueueToken> deleteFirstWaitingToken(DeleteFirstWaitingTokenCommand command) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(command.getQueueName());
        String tokenValue = sortedSet.first();
        if (sortedSet.first() == null) {
            return Optional.empty();
        }
        sortedSet.remove(tokenValue);
        WaitingQueueToken deletedToken = WaitingQueueToken.valueOf(command.getPerformanceId(), tokenValue);
        return Optional.of(deletedToken);
    }

}

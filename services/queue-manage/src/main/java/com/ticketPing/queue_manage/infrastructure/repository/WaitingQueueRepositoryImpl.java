package com.ticketPing.queue_manage.infrastructure.repository;

import com.ticketPing.queue_manage.domain.command.waitingQueue.InsertWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.DeleteFirstWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.FindWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RScoredSortedSet;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {

    private final RedisRepository redisRepository;

    @Override
    public boolean insertWaitingQueueToken(InsertWaitingQueueTokenCommand command) {
        RScoredSortedSet<String> sortedSet = redisRepository.getScoredSortedSet(command.getQueueName());
        return sortedSet.add(command.getScore(), command.getTokenValue());
    }

    @Override
    public Optional<WaitingQueueToken> findWaitingQueueToken(FindWaitingQueueTokenCommand command) {
        RScoredSortedSet<String> sortedSet = redisRepository.getScoredSortedSet(command.getQueueName());
        try {
            int rank = sortedSet.rank(command.getTokenValue()) + 1;
            int size = sortedSet.size();
            WaitingQueueToken token = WaitingQueueToken.withPosition(command.getUserId(), command.getPerformanceId(), command.getTokenValue(), rank, size);
            return Optional.of(token);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<WaitingQueueToken> deleteFirstWaitingQueueToken(DeleteFirstWaitingQueueTokenCommand command) {
        RScoredSortedSet<String> sortedSet = redisRepository.getScoredSortedSet(command.getQueueName());
        String tokenValue = sortedSet.first();
        if (sortedSet.first() == null) {
            return Optional.empty();
        }
        sortedSet.remove(tokenValue);
        WaitingQueueToken deletedToken = WaitingQueueToken.valueOf(command.getPerformanceId(), tokenValue);
        return Optional.of(deletedToken);
    }

}

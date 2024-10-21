package com.ticketPing.queue_manage.infrastructure.repository;

import com.ticketPing.queue_manage.domain.command.waitingQueue.DeleteFirstWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.FindWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.InsertWaitingQueueTokenCommand;
import com.ticketPing.queue_manage.domain.model.QueueToken;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import com.ticketPing.queue_manage.domain.model.WorkingQueueToken;
import com.ticketPing.queue_manage.domain.repository.WaitingQueueRepository;
import com.ticketPing.queue_manage.infrastructure.repository.script.InsertWaitingQueueTokenScript;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RScoredSortedSet;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {

    private final RedisRepository redisRepository;
    private final InsertWaitingQueueTokenScript script;

    @Override
    public QueueToken insertWaitingQueueToken(InsertWaitingQueueTokenCommand command) {
        if (script.execute(command)) {
            return WorkingQueueToken.create(command.getUserId(), command.getPerformanceId());
        }
        return WaitingQueueToken.create(command.getUserId(), command.getPerformanceId());
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

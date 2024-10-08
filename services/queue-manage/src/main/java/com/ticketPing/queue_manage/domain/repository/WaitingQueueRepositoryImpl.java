package com.ticketPing.queue_manage.domain.repository;

import static com.ticketPing.queue_manage.presentaion.cases.QueueErrorCase.USER_NOT_FOUND;
import static com.ticketPing.queue_manage.domain.model.WaitingQueueToken.tokenWithPosition;

import com.ticketPing.queue_manage.domain.command.waitingQueue.DequeueCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.EnqueueCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.RetrieveTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.RetrieveTopTokensCommand;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import common.exception.ApplicationException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
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

    @Override
    public List<WaitingQueueToken> retrieveTopTokens(RetrieveTopTokensCommand command) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(command.getQueueName());
        Collection<String> tokenValues = sortedSet.valueRange(0, command.getCount() - 1);
        return tokenValues.stream()
                .map(WaitingQueueToken::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public boolean dequeue(DequeueCommand command) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(command.getQueueName());
        return sortedSet.remove(command.getUser());
    }

}

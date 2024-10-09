package com.ticketPing.queue_manage.domain.repository;

import static com.ticketPing.queue_manage.domain.model.WaitingQueueToken.tokenWithPosition;

import com.ticketPing.queue_manage.domain.command.waitingQueue.DequeueWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.EnqueueWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.RetrieveWaitingTokenCommand;
import com.ticketPing.queue_manage.domain.command.waitingQueue.RetrieveTopWaitingTokensCommand;
import com.ticketPing.queue_manage.domain.model.WaitingQueueToken;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
    public boolean enqueueWaitingToken(EnqueueWaitingTokenCommand command) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(command.getQueueName());
        return sortedSet.add(command.getScore(), command.getTokenValue());
    }

    @Override
    public Optional<WaitingQueueToken> retrieveWaitingToken(RetrieveWaitingTokenCommand command) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(command.getQueueName());
        int totalSize = sortedSet.size();
        try {
            int position = sortedSet.rank(command.getTokenValue()) + 1;
            return Optional.of(tokenWithPosition(command.getUserId(), command.getTokenValue(), position, totalSize));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<WaitingQueueToken> retrieveTopWaitingTokens(RetrieveTopWaitingTokensCommand command) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(command.getQueueName());
        Collection<String> tokenValues = sortedSet.valueRange(0, command.getCount() - 1);
        return tokenValues.stream()
                .map(WaitingQueueToken::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    public boolean dequeueWaitingToken(DequeueWaitingTokenCommand command) {
        RScoredSortedSet<String> sortedSet = redissonClient.getScoredSortedSet(command.getQueueName());
        return sortedSet.remove(command.getTokenValue());
    }

}

package com.ticketping.gateway.infrastructure.repository;

import static com.ticketping.gateway.infrastructure.enums.RedisKeyPrefix.AVAILABLE_SEATS;
import static com.ticketping.gateway.infrastructure.enums.RedisKeyPrefix.WAITING_QUEUE;

import com.ticketping.gateway.domain.repository.QueueCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QueueCheckRepositoryImpl implements QueueCheckRepository {

    private final RedisRepository redisRepository;

    @Override
    public int getAvailableSeats(String performanceId) {
        String key = AVAILABLE_SEATS.getValue() + performanceId;
        return (int) redisRepository.getValue(key);
    }

    @Override
    public long getWaitingUsers(String performanceId) {
        String key = WAITING_QUEUE.getValue() + performanceId;
        return redisRepository.getSortedSetSize(key);
    }

    @Override
    public Boolean findWorkingToken(String tokenValue) {
        return redisRepository.hasKey(tokenValue);
    }

}

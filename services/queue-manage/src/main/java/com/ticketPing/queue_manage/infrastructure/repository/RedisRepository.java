package com.ticketPing.queue_manage.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedissonClient redissonClient;

    public RScoredSortedSet<String> getScoredSortedSet(String key) {
        return redissonClient.getScoredSortedSet(key);
    }

    public RAtomicLong getAtomicLong(String key) {
        return redissonClient.getAtomicLong(key);
    }

    public RBucket<String> getBucket(String key) {
        return redissonClient.getBucket(key, StringCodec.INSTANCE);
    }

}

package com.ticketPing.queue_manage.infrastructure.repository;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLongReactive;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedissonReactiveClient redissonClient;

    public Mono<Integer> getSortedSetSize(String key) {
        RScoredSortedSetReactive<String> sortedSet = redissonClient.getScoredSortedSet(key, StringCodec.INSTANCE);
        return sortedSet.size();
    }

    public Mono<Integer> getMemberRankFromSortedSet(String key, String member) {
        RScoredSortedSetReactive<String> sortedSet = redissonClient.getScoredSortedSet(key, StringCodec.INSTANCE);
        return sortedSet.rank(member);
    }

    public Mono<String> getFirstMemberFromSortedSet(String key) {
        RScoredSortedSetReactive<String> sortedSet = redissonClient.getScoredSortedSet(key, StringCodec.INSTANCE);
        return sortedSet.first();
    }

    public Mono<Boolean> deleteMemberFromSortedSet(String key, String member) {
        RScoredSortedSetReactive<String> sortedSet = redissonClient.getScoredSortedSet(key, StringCodec.INSTANCE);
        return sortedSet.remove(member);
    }

    public Mono<Void> setElementWithTTL(String key, String value, long ttlInMinutes) {
        RBucketReactive<String> bucket = redissonClient.getBucket(key, StringCodec.INSTANCE);
        return bucket.set(value, ttlInMinutes, TimeUnit.MINUTES);
    }

    public Mono<String> getElementFromBucket(String key) {
        RBucketReactive<String> bucket = redissonClient.getBucket(key, StringCodec.INSTANCE);
        return bucket.get();
    }

    public Mono<Long> getElementRemainingTTL(String key) {
        RBucketReactive<String> bucket = redissonClient.getBucket(key, StringCodec.INSTANCE);
        return bucket.remainTimeToLive()
                .filter(ttl -> ttl != null && ttl > 0);
    }

    public Mono<Boolean> deleteElementFromBucket(String key) {
        RBucketReactive<String> bucket = redissonClient.getBucket(key, StringCodec.INSTANCE);
        return bucket.delete();
    }

    public Mono<Long> incrementCounter(String key) {
        RAtomicLongReactive atomicLong = redissonClient.getAtomicLong(key);
        return atomicLong.incrementAndGet();
    }

    public Mono<Long> decrementCounter(String key) {
        RAtomicLongReactive atomicLong = redissonClient.getAtomicLong(key);
        return atomicLong.decrementAndGet();
    }

}

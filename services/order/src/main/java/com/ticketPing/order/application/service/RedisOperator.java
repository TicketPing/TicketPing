package com.ticketPing.order.application.service;

import com.ticketPing.order.presentation.response.exception.OrderExceptionCase;
import common.exception.ApplicationException;
import java.time.Duration;
import java.util.Map;
import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisOperator {

    private final RedissonClient redissonClient;

    public Boolean exists(String key) {
        return tryOperation(() -> redissonClient.getBucket(key, StringCodec.INSTANCE).isExists());
    }

    public void expire(String key, long expirationSeconds) {
        tryOperation(() -> redissonClient.getBucket(key, StringCodec.INSTANCE)
            .expire(Duration.ofSeconds(expirationSeconds)));
    }

    public String get(String key) {
        return tryOperation(() -> {
            RBucket<String> bucket = redissonClient.getBucket(key, StringCodec.INSTANCE);
            return bucket.get();
        });
    }

    public void set(String key, String value, long expirationSeconds) {
        tryOperation(() -> {
            RBucket<String> bucket = redissonClient.getBucket(key, StringCodec.INSTANCE);
            bucket.set(value, Duration.ofSeconds(expirationSeconds));
            return null;
        });
    }

    public void set(String key, String value) {
        tryOperation(() -> {
            RBucket<String> bucket = redissonClient.getBucket(key, StringCodec.INSTANCE);
            bucket.set(value);
            return null;
        });
    }

    public void delete(String key) {
        tryOperation(() -> {
            RBucket<String> bucket = redissonClient.getBucket(key, StringCodec.INSTANCE);
            bucket.delete();
            return null;
        });
    }

    public void setIfAbsentWithTTL(String key, String value, long expirationSeconds) {
        tryOperation(() -> {
            RBucket<String> bucket = redissonClient.getBucket(key, StringCodec.INSTANCE);
            if (bucket.isExists()) {
                return null; // 이미 존재하는 경우 아무 작업도 하지 않음
            }
            bucket.set(value, Duration.ofSeconds(expirationSeconds));
            return null;
        });
    }

    public void hmset(String seatId, Map<String, String> seatData) {
        tryOperation(() -> {
            RMap<String, String> redisMap = redissonClient.getMap(seatId);
            redisMap.putAll(seatData);
            return null;
        });
    }

    private <T> T tryOperation(Supplier<T> operation) {
        try {
            return operation.get();
        } catch (Exception e) {
            log.error("Redis operation failed: {}", e.getMessage(), e);
            throw new ApplicationException(OrderExceptionCase.REDIS_SERVICE_UNAVAILABLE);
        }
    }
}

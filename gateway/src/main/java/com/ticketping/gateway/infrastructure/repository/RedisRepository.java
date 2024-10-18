package com.ticketping.gateway.infrastructure.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Long getSortedSetSize(String key) {
        return redisTemplate.opsForZSet().size(key);
    }

    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

}

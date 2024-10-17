package com.ticketPing.order.infrastructure.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Boolean hasKey(String key) { return redisTemplate.hasKey(key); }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void setTtl(String key, String value, int time, TimeUnit unit) {
        redisTemplate.opsForValue().set(key,value,time,unit);
    }

}

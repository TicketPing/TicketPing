package com.ticketPing.payment.infrastructure.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public Long getTTL(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

}

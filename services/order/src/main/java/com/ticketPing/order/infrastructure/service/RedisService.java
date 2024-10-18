package com.ticketPing.order.infrastructure.service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
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

    public boolean keysStartingWith(String prefix) {
        ScanOptions options = ScanOptions.scanOptions().match(prefix + "*").count(100).build();
        Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(redisConnection -> {
            return redisConnection.scan(options);
        });

        try {
            while (cursor.hasNext()) {
                cursor.next();
                return true; // 키가 하나라도 존재하면 true 반환
            }
        } finally {
            cursor.close(); // Cursor를 반드시 닫아야 합니다.
        }

        return false; // 키가 존재하지 않으면 false 반환
    }


}

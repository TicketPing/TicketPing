package com.ticketPing.order.infrastructure.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    public Boolean keysStartingWith(String prefix) {
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

    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    public List<String> getKeysStartingWith(String prefix) {
        List<String> keys = new ArrayList<>();
        ScanOptions options = ScanOptions.scanOptions().match(prefix + "*").count(100).build();
        Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(redisConnection -> {
            return redisConnection.scan(options);
        });

        try {
            while (cursor.hasNext()) {
                // 키를 문자열로 변환하여 리스트에 추가
                keys.add(new String(cursor.next()));
            }
        } finally {
            cursor.close(); // Cursor를 반드시 닫아야 합니다.
        }

        return keys; // 모든 키 반환
    }

    public Boolean hasMultipleKeysStartingWith(String prefix) {
        ScanOptions options = ScanOptions.scanOptions().match(prefix + "*").count(100).build();
        Cursor<byte[]> cursor = redisTemplate.executeWithStickyConnection(redisConnection -> {
            return redisConnection.scan(options);
        });

        int count = 0; // 키의 개수를 셀 변수

        try {
            while (cursor.hasNext()) {
                cursor.next();
                count++;
                if (count >= 2) {
                    return true; // 두 개 이상의 키가 존재하면 true 반환
                }
            }
        } finally {
            cursor.close(); // Cursor를 반드시 닫아야 합니다.
        }

        return false; // 키가 두 개 이상 존재하지 않으면 false 반환
    }


}

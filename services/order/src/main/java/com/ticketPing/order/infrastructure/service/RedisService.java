package com.ticketPing.order.infrastructure.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.security.AuthorizationAuditListener;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.ticketPing.order.presentation.cases.exception.OrderExceptionCase.SOLD_OUT;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setValueWithTTL(String key, Object value, int seconds) {
        redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
    }

    public <T> T getValueAsClass(String key, Class<T> clazz) {
        return objectMapper.convertValue(redisTemplate.opsForValue().get(key), clazz);
    }

    public void deleteKey(String key) {
        redisTemplate.delete(key);
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

    public void decreaseCounter(UUID performanceId) {
        String key = "AvailableSeats:" + performanceId;
        Long updatedValue = redisTemplate.opsForValue().decrement(key);
        if(updatedValue != null && updatedValue < 0) {
            throw new ApplicationException(SOLD_OUT);
        }
        System.out.println("남은 좌석 수 : " + updatedValue);
    }
}

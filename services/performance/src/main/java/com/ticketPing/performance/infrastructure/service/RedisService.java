package com.ticketPing.performance.infrastructure.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public Set<String> getKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setValues(Map<String, Object> map) {
        redisTemplate.opsForValue().multiSet(map);
    }

    public <T> List<T> getValuesAsClass(List<String> keys, Class<T> clazz) {
        return redisTemplate.opsForValue().multiGet(keys)
                .stream().map(v -> objectMapper.convertValue(v, clazz)).toList();
    }

}

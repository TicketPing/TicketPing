package com.ticketPing.payment.infrastructure.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class RedisLockService {
    private static final long LOCK_EXPIRATION = 3000; // 락 만료 시간 (3초)
    private static final long RETRY_DELAY = 100;     // 재시도 대기 시간 (100ms)

    private final RedisTemplate<String, String> redisTemplate;

    public RedisLockService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    // Spin Lock 메서드
    public boolean tryLock(String key) {
        String lockValue = String.valueOf(System.currentTimeMillis() + LOCK_EXPIRATION);

        // 락 시도
        Boolean success = redisTemplate.opsForValue().setIfAbsent(key, lockValue);

        // 락 획득에 성공한 경우
        if (Boolean.TRUE.equals(success)) {
            redisTemplate.expire(key, LOCK_EXPIRATION, TimeUnit.MILLISECONDS); // 만료 시간 설정
            return true;
        }

        // 기존 락 확인
        String currentValue = redisTemplate.opsForValue().get(key);

        // 락이 만료되었는지 확인
        if (currentValue != null && Long.parseLong(currentValue) < System.currentTimeMillis()) {
            // 만료된 락을 새로운 값으로 교체 (Atomic)
            String oldValue = redisTemplate.opsForValue().getAndSet(key, lockValue);

            if (oldValue != null && oldValue.equals(currentValue)) {
                redisTemplate.expire(key, LOCK_EXPIRATION, TimeUnit.MILLISECONDS); // 만료 시간 갱신
                return true;
            }
        }

        // 락을 얻지 못한 경우
        return false;
    }

    // Spin Lock을 걸고 일정 시간 대기
    public boolean acquireLockWithSpin(String key, long timeoutMillis) {
        long end = System.currentTimeMillis() + timeoutMillis;

        // 타임아웃 시간까지 반복 시도
        while (System.currentTimeMillis() < end) {
            if (tryLock(key)) {
                return true;  // 락을 획득한 경우
            }
            try {
                Thread.sleep(RETRY_DELAY);  // 대기 후 재시도
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // 타임아웃 시간 내에 락을 얻지 못한 경우
        return false;
    }

    // 락 해제
    public void unlock(String key) {
        redisTemplate.delete(key);
    }

    public boolean verifyTtl(UUID orderId, UUID seatId) {
        return true;
    }
}

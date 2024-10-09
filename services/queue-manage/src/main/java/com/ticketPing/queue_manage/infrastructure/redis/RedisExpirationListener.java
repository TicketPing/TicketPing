package com.ticketPing.queue_manage.infrastructure.redis;

import com.ticketPing.queue_manage.application.service.WorkingQueueApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisExpirationListener implements MessageListener {

    private final WorkingQueueApplicationService workingQueueService;
    private final RedissonClient redissonClient;

    @Override
    public void onMessage(final Message tokenValue, final byte[] pattern) {
        log.info("WorkingQueueToken has expired: {}", tokenValue);

        String leaderKey = tokenValue.toString();
        RLock leaderLock = redissonClient.getLock(leaderKey);

        // 리더 선출 시도
        if (leaderLock.tryLock()) {
            try {
                // 리더로 선출된 인스턴스만 작업 수행
                log.info("This instance is the leader, processing job: {}", leaderKey);
                workingQueueService.processQueueTransfer(tokenValue.toString());
            } finally {
                leaderLock.unlock();
            }
        } else {
            // 리더 선출에 실패한 인스턴스는 아무 작업도 수행하지 않음
            log.info("Failed to acquire lock for key: {}", leaderKey);
        }
    }

}
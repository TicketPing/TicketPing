package com.ticketPing.queue_manage.infrastructure.redis;

import com.ticketPing.queue_manage.application.service.WorkingQueueApplicationService;
import java.util.concurrent.TimeUnit;
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

    private static final String TOKEN_PREFIX = "Token:";

    private final WorkingQueueApplicationService workingQueueService;
    private final RedissonClient redissonClient;

    @Override
    public void onMessage(final Message tokenValue, final byte[] pattern) {
        // 작업열 토큰 확인
        if (tokenValue.toString().startsWith(TOKEN_PREFIX)) {
            log.info("WorkingQueueToken has expired: {}", tokenValue);

            String leaderKey = "LeaderKey:" + tokenValue;
            RLock leaderLock = redissonClient.getLock(leaderKey);

            tryToLeader(tokenValue, leaderLock);
        }
    }

    // 리더 선출 시도
    private void tryToLeader(Message tokenValue, RLock leaderLock) {
        try {
            if (leaderLock.tryLock(0, 5, TimeUnit.SECONDS)) {
                try {
                    // 리더로 선출된 인스턴스만 작업 수행
                    log.info("This instance is the leader, processing job..");
                    workingQueueService.processQueueTransfer(tokenValue.toString());
                } finally {
                    if (leaderLock.isHeldByCurrentThread()) {
                        leaderLock.unlock();
                    }
                }
            } else {
                log.info("Failed to acquire lock for key!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
package com.ticketPing.queue_manage.infrastructure.config.listener;

import static com.ticketPing.queue_manage.domain.model.enums.DeleteWorkingTokenCase.TOKEN_EXPIRED;
import static com.ticketPing.queue_manage.infrastructure.enums.RedisKeyPrefix.LEADER_KEY;
import static com.ticketPing.queue_manage.infrastructure.enums.RedisKeyPrefix.TOKEN_VALUE;

import com.ticketPing.queue_manage.application.service.WorkingQueueService;
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

    private final WorkingQueueService workingQueueService;
    private final RedissonClient redissonClient;

    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        // 작업열 토큰 확인
        String tokenValue = message.toString();
        if (tokenValue.startsWith(TOKEN_VALUE.getValue())) {
            log.info("WorkingQueueToken has expired: {}", tokenValue);

            String leaderKey = LEADER_KEY.getValue() + tokenValue;
            RLock leaderLock = redissonClient.getLock(leaderKey);

            tryToLeader(tokenValue, leaderLock);
        }
    }

    // 리더 선출 시도
    private void tryToLeader(String tokenValue, RLock leaderLock) {
        try {
            if (leaderLock.tryLock(0, 5, TimeUnit.SECONDS)) {
                try {
                    // 리더로 선출된 인스턴스만 작업 수행
                    log.info("This instance is the leader, processing job..");
                    workingQueueService.processQueueTransfer(TOKEN_EXPIRED, tokenValue);
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
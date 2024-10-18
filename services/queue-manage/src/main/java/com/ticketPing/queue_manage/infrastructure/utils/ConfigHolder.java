package com.ticketPing.queue_manage.infrastructure.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 환경 설정
 */
@Component
public class ConfigHolder {

    private static String tokenValueSecretKey;
    private static int workingQueueMaxSize;
    private static int workingQueueTokenTTL;

    @Value("${token-value.secret-key}")
    public void setSecretKey(String secretKey) {
        tokenValueSecretKey = secretKey;
    }

    @Value("${working-queue.max-size}")
    public void setWorkingQueueMaxSize(int maxSize) {
        workingQueueMaxSize = maxSize;
    }

    @Value("${working-queue.token-ttl}")
    public void setWorkingQueueTokenTTL(int tokenTTL) {
        workingQueueTokenTTL = tokenTTL;
    }

    public static String tokenValueSecretKey() {
        return tokenValueSecretKey;
    }

    public static int workingQueueMaxSize() {
        return workingQueueMaxSize;
    }

    public static int workingQueueTokenTTL() {
        return workingQueueTokenTTL;
    }

}
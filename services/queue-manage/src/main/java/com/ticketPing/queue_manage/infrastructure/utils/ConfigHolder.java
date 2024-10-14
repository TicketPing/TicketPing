package com.ticketPing.queue_manage.infrastructure.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 환경 설정
 */
@Component
public class ConfigHolder {

    private static String serverPort;
    private static int workingQueueMaxSize;
    private static int workingQueueTokenTTL;

    @Value("${server.port}")
    public void setServerPort(String port) {
        serverPort = port;
    }

    @Value("${working-queue.max-size}")
    public void setWorkingQueueMaxSize(int maxSize) {
        workingQueueMaxSize = maxSize;
    }

    @Value("${working-queue.token-ttl}")
    public void setWorkingQueueTokenTTL(int tokenTTL) {
        workingQueueTokenTTL = tokenTTL;
    }

    public static String serverPort() {
        return serverPort;
    }

    public static int workingQueueMaxSize() {
        return workingQueueMaxSize;
    }

    public static int workingQueueTokenTTL() {
        return workingQueueTokenTTL;
    }

}
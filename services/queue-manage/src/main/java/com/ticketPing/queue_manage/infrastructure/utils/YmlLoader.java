package com.ticketPing.queue_manage.infrastructure.utils;

import org.springframework.beans.factory.annotation.Value;


/**
 * 환경 설정
 */
public class YmlLoader {

    @Value("${server.port}")
    private static String serverPort;

    @Value("${redis.host}")
    private static String redisHost;

    @Value("${redis.port}")
    private static String redisPort;

    @Value("${redis.host}")
    private static int workingQueueMaxSize;

    @Value("${redis.port}")
    private static int workingQueueTokenTTL;

    private static String serverPort() {
        return serverPort;
    }

    public static String redisHost() {
        return redisHost;
    }

    public static String redisPort() {
        return redisPort;
    }

    public static int workingQueueMaxSize() {
        return workingQueueMaxSize;
    }

    public static int workingQueueTokenTTL() {
        return workingQueueTokenTTL;
    }

}
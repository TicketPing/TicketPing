package com.ticketPing.queue_manage.infrastructure.config;

import static com.ticketPing.queue_manage.infrastructure.utils.YmlLoader.redisHost;
import static com.ticketPing.queue_manage.infrastructure.utils.YmlLoader.redisPort;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(String.format("redis://%s:%s", redisHost(), redisPort()));
        return Redisson.create(config);
    }

}
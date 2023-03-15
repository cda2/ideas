package com.example.redis_key_expired_event_example.expired.config;


import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisServer;
import org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Profile("default")
@TestConfiguration
@EnableRedisRepositories(enableKeyspaceEvents = EnableKeyspaceEvents.ON_STARTUP)
@RequiredArgsConstructor
@Slf4j
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedisServer redisServer() {
        log.warn("Starting redis server");
        return new RedisServer(redisProperties.getHost(), redisProperties.getPort());
    }
}

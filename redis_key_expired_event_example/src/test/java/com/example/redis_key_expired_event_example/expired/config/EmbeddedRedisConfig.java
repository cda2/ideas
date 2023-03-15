package com.example.redis_key_expired_event_example.expired.config;

import java.net.ServerSocket;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisKeyValueAdapter.EnableKeyspaceEvents;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import redis.embedded.RedisServer;
import redis.embedded.RedisServerBuilder;

@TestConfiguration
@RequiredArgsConstructor
@EnableRedisRepositories(enableKeyspaceEvents = EnableKeyspaceEvents.ON_STARTUP)
@Profile("embedded")
@Slf4j
public class EmbeddedRedisConfig {

    private final RedisProperties redisProperties;
    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() {
        log.warn("Starting embedded redis");
        RedisServerBuilder serverBuilder = new RedisServerBuilder();
        serverBuilder.setting("maxmemory 128M");
        int port = redisProperties.getPort();
        serverBuilder.port((port > 0 && port < 65536 && port != 6379) ? port : getRandomPort());
        redisServer = serverBuilder.build();
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

    public static int getRandomPort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);
            return socket.getLocalPort();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

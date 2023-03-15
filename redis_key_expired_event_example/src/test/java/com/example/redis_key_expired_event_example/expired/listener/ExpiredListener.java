package com.example.redis_key_expired_event_example.expired.listener;


import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisKeyExpiredEvent;
import org.springframework.stereotype.Component;

import com.example.redis_key_expired_event_example.expired.domain.Domain;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Getter
public class ExpiredListener {

    private int domainCount = 0;
    private int othersCount = 0;

    @EventListener
    public void handleExpiredEvent(RedisKeyExpiredEvent<Domain> event) {
        if (event.getValue() instanceof Domain domainValue) {
            log.info("Domain event received: {}", event);
            log.info("Domain event source: {}", event.getSource());
            log.info("Domain event keyspace: {}", event.getKeyspace());
            log.info("Domain event channel: {}", event.getChannel());
            log.info("Domain event id: {}", event.getId());
            log.info("Domain event timestamp: {}", event.getTimestamp());
            log.info("Domain event class: {}", event.getClass());
            log.info("Domain event value: {}", domainValue);
            domainCount += 1;
        }
        else {
            log.info("Unknown event received: {}", event);
            log.info("Unknown event source: {}", event.getSource());
            log.info("Unknown event keyspace: {}", event.getKeyspace());
            log.info("Unknown event channel: {}", event.getChannel());
            log.info("Unknown event id: {}", event.getId());
            log.info("Unknown event timestamp: {}", event.getTimestamp());
            log.info("Unknown event class: {}", event.getClass());
            log.info("Unknown event value: {}", event.getValue());
            othersCount += 1;
        }
    }

    public void resetCount() {
        domainCount = 0;
        othersCount = 0;
    }
}

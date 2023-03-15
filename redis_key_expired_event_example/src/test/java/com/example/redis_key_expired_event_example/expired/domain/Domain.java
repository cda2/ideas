package com.example.redis_key_expired_event_example.expired.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Value;


@RedisHash(timeToLive = 2L)
@Value
public class Domain implements Serializable {

    @Id
    Long id;
    String name;

    public Domain(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

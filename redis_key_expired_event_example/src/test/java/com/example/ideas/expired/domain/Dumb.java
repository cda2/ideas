package com.example.ideas.expired.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Value;

@RedisHash(timeToLive = 3L)
@Value
public class Dumb implements Serializable {


    @Id
    Long id;

    Double value;

    public Dumb(Long id, Double value) {
        this.id = id;
        this.value = value;
    }
}

package com.example.redis_key_expired_event_example.expired.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.redis_key_expired_event_example.expired.domain.Domain;

public interface DomainRepository extends CrudRepository<Domain, Long> {

}

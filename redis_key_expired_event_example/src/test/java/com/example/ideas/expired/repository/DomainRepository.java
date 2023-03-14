package com.example.ideas.expired.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.ideas.expired.domain.Domain;

public interface DomainRepository extends CrudRepository<Domain, Long> {

}

package com.example.ideas.expired.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.ideas.expired.domain.Dumb;

public interface DumbRepository extends CrudRepository<Dumb, Long> {

}

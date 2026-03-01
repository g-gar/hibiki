package com.ggar.hibiki.core.identity.persistence.repository;

import com.ggar.hibiki.core.identity.persistence.entity.UserEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveNeo4jRepository<UserEntity, String> {
    Mono<UserEntity> findByUsername(String username);

    Mono<UserEntity> findByEmail(String email);
}

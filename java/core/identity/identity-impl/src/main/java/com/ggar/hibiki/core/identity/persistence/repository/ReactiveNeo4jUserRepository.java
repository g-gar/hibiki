package com.ggar.hibiki.core.identity.persistence.repository;

import com.ggar.hibiki.core.identity.persistence.entity.UserEntity;
import java.util.UUID;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReactiveNeo4jUserRepository extends ReactiveNeo4jRepository<UserEntity, UUID> {
    Mono<UserEntity> findByUsername(String username);

    Mono<UserEntity> findByEmail(String email);
}

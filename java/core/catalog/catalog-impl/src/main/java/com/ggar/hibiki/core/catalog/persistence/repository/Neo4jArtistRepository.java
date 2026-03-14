package com.ggar.hibiki.core.catalog.persistence.repository;

import com.ggar.hibiki.core.catalog.persistence.entity.ArtistEntity;
import java.util.UUID;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface Neo4jArtistRepository extends ReactiveNeo4jRepository<ArtistEntity, UUID> {

    Mono<ArtistEntity> save(ArtistEntity entity);

    Mono<Boolean> existsById(UUID id);

    Mono<Long> count();

    Mono<ArtistEntity> findByNameIgnoreCase(String name);

    Mono<ArtistEntity> findByIsni(String isni);
}

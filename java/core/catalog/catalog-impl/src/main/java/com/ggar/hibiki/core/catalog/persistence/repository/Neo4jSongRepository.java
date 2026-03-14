package com.ggar.hibiki.core.catalog.persistence.repository;

import com.ggar.hibiki.core.catalog.persistence.entity.SongEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Repository
public interface Neo4jSongRepository extends ReactiveNeo4jRepository<SongEntity, UUID> {

    Mono<SongEntity> findById(UUID id);

    Mono<SongEntity> save(SongEntity entity);

    Mono<SongEntity> findByIsrc(String isrc);
}

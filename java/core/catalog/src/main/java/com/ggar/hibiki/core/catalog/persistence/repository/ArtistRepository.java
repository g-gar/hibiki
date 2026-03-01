package com.ggar.hibiki.core.catalog.persistence.repository;

import com.ggar.hibiki.core.catalog.persistence.entity.ArtistEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ArtistRepository extends ReactiveNeo4jRepository<ArtistEntity, String> {

    Mono<ArtistEntity> findByNameIgnoreCase(String name);

    Mono<ArtistEntity> findByIsni(String isni);

}

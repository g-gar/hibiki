package com.ggar.hibiki.core.catalog.persistence.repository;

import com.ggar.hibiki.core.catalog.persistence.entity.AlbumEntity;
import java.util.UUID;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface Neo4jAlbumRepository extends ReactiveNeo4jRepository<AlbumEntity, UUID> {

    Mono<AlbumEntity> save(AlbumEntity entity);

    Mono<Boolean> existsById(UUID id);

    Mono<AlbumEntity> findById(UUID id);

    Mono<AlbumEntity> findByTitleIgnoreCase(String title);

    Mono<AlbumEntity> findByTitleIgnoreCaseAndArtistNameIgnoreCase(String title, String artistName);

    Mono<AlbumEntity> findByBarcode(String barcode);

    @Query("MATCH (al:Album)-[:RELEASED_BY]->(a:Artist {id: $artistId}) DETACH DELETE al")
    Mono<Void> deleteByArtistId(UUID artistId);
}

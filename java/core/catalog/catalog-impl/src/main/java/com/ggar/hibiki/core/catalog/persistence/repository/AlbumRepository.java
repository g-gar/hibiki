package com.ggar.hibiki.core.catalog.persistence.repository;

import com.ggar.hibiki.core.catalog.persistence.entity.AlbumEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AlbumRepository extends ReactiveNeo4jRepository<AlbumEntity, String> {

    Mono<AlbumEntity> findByTitleIgnoreCase(String title);

    Mono<AlbumEntity> findByTitleIgnoreCaseAndArtistNameIgnoreCase(String title, String artistName);

    Mono<AlbumEntity> findByBarcode(String barcode);
}

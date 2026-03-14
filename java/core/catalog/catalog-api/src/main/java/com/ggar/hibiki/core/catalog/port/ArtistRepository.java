package com.ggar.hibiki.core.catalog.port;

import com.ggar.hibiki.core.catalog.model.Artist;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface ArtistRepository {
    Mono<Artist> save(Artist artist);

    Mono<Artist> findById(UUID id);

    Mono<Artist> findByName(String name);

    Mono<Artist> findByIsni(String isni);

    Mono<Boolean> existsById(UUID id);

    Mono<Void> deleteById(UUID id);

    Mono<Long> count();
}

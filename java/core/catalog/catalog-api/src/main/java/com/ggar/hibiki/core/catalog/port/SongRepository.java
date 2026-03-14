package com.ggar.hibiki.core.catalog.port;

import com.ggar.hibiki.core.catalog.model.Song;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface SongRepository {
    Mono<Song> findById(UUID id);

    Mono<Song> save(Song song);

    Mono<Boolean> existsById(UUID id);

    Mono<Song> findByIsrc(String isrc);

    Mono<Void> deleteById(UUID id);

    Mono<Long> count();
}

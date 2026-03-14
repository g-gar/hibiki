package com.ggar.hibiki.core.catalog.port;

import com.ggar.hibiki.core.catalog.model.Album;
import java.util.UUID;
import reactor.core.publisher.Mono;

public interface AlbumRepository {
    Mono<Album> save(Album album);

    Mono<Boolean> existsById(UUID id);

    Mono<Album> findById(UUID id);

    Mono<Album> findByTitle(String title);

    Mono<Album> findByTitleAndArtist(String title, String artistName);

    Mono<Album> findByBarcode(String barcode);

    Mono<Void> deleteById(UUID id);

    Mono<Void> deleteByArtistId(UUID artistId);

    Mono<Long> count();
}

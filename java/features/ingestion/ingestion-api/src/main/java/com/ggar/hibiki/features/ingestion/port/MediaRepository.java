package com.ggar.hibiki.features.ingestion.port;

import com.ggar.hibiki.features.ingestion.model.Media;
import com.ggar.hibiki.features.ingestion.model.MediaId;
import reactor.core.publisher.Mono;

/**
 * Port for persisting media entities.
 */
public interface MediaRepository {
    Mono<Media> save(Media media);

    Mono<Media> findById(MediaId id);
}

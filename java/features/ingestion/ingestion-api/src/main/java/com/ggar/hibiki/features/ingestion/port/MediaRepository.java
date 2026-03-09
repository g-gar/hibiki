package com.ggar.hibiki.features.ingestion.port;

import com.ggar.hibiki.features.ingestion.model.Media;
import java.util.UUID;
import reactor.core.publisher.Mono;

/**
 * Port for persisting media entities.
 */
public interface MediaRepository {
    Mono<Media> save(Media media);

    Mono<Media> findById(UUID id);
}

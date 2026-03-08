package com.ggar.hibiki.features.ingestion.port;

import com.ggar.hibiki.features.ingestion.model.Media;
import reactor.core.publisher.Mono;

/**
 * Port interface for media persistence operations.
 *
 * <p>Defines the contract for saving and retrieving {@link Media} entities, to be implemented
 * by the infrastructure layer.
 */
public interface MediaRepository {

    /**
     * Persists a given media entity.
     *
     * @param media the media entity to save
     * @return a {@link Mono} emitting the saved media entity
     */
    Mono<Media> save(Media media);
}

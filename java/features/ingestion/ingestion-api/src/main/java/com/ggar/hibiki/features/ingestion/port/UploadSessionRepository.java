package com.ggar.hibiki.features.ingestion.port;

import com.ggar.hibiki.features.ingestion.model.UploadSession;
import java.util.UUID;
import reactor.core.publisher.Mono;

/**
 * Port for persisting upload sessions.
 */
public interface UploadSessionRepository {
    Mono<UploadSession> save(UploadSession session);

    Mono<UploadSession> findById(UUID id);

    Mono<Void> deleteById(UUID id);
}

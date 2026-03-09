package com.ggar.hibiki.features.ingestion.port;

import com.ggar.hibiki.features.ingestion.model.UploadSession;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import reactor.core.publisher.Mono;

/**
 * Port for persisting upload sessions.
 */
public interface UploadSessionRepository {
    Mono<UploadSession> save(UploadSession session);

    Mono<UploadSession> findById(UploadSessionId id);

    Mono<Void> deleteById(UploadSessionId id);
}

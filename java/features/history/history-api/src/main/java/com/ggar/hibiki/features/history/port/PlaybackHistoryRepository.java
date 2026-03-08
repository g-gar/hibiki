package com.ggar.hibiki.features.history.port;

import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import java.util.UUID;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Outbound port for managing playback history persistence.
 */
public interface PlaybackHistoryRepository {
    Mono<PlaybackHistoryEntry> save(PlaybackHistoryEntry entry);

    Flux<PlaybackHistoryEntry> findByUserId(UUID userId);
}

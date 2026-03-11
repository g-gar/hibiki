package com.ggar.hibiki.features.history.port;

import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import com.ggar.hibiki.features.history.model.PlaybackHistoryId;
import com.ggar.hibiki.features.history.model.UserId;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Repository interface for managing playback history entries.
 */
public interface PlaybackHistoryRepository {
    Mono<PlaybackHistoryEntry> save(PlaybackHistoryEntry entry);

    Flux<PlaybackHistoryEntry> findByUserId(UserId userId);

    Mono<Void> deleteById(PlaybackHistoryId id);
}

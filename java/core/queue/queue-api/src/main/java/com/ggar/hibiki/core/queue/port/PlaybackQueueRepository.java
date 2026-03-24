package com.ggar.hibiki.core.queue.port;

import com.ggar.hibiki.core.queue.model.PlaybackQueue;
import com.ggar.hibiki.core.queue.model.SessionId;
import reactor.core.publisher.Mono;

/**
 * Output port for playback queue persistence.
 * Implementation uses Redis with TTL linked to session lifetime.
 */
public interface PlaybackQueueRepository {

    Mono<PlaybackQueue> findBySessionId(SessionId sessionId);

    Mono<PlaybackQueue> save(PlaybackQueue queue);

    Mono<Void> deleteBySessionId(SessionId sessionId);
}

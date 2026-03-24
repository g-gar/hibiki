package com.ggar.hibiki.core.queue.infrastructure.persistence.repository;

import com.ggar.hibiki.core.queue.infrastructure.persistence.entity.PlaybackQueueEntity;
import com.ggar.hibiki.core.queue.infrastructure.persistence.mapper.PlaybackQueueMapper;
import com.ggar.hibiki.core.queue.model.PlaybackQueue;
import com.ggar.hibiki.core.queue.model.SessionId;
import com.ggar.hibiki.core.queue.port.PlaybackQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class RedisPlaybackQueueRepository implements PlaybackQueueRepository {

    private final ReactiveRedisTemplate<String, PlaybackQueueEntity> redisTemplate;
    private final PlaybackQueueMapper mapper;
    private static final String KEY_PREFIX = "playback_queue:";

    @Override
    public Mono<PlaybackQueue> findBySessionId(SessionId sessionId) {
        // En Redis indexamos por sessionId o usamos la sessionId como parte de la clave si la relación es 1:1
        // Para simplificar, usaremos la sessionId en la clave
        return redisTemplate
                .opsForValue()
                .get(KEY_PREFIX + sessionId.value().toString())
                .map(mapper::toDomain);
    }

    @Override
    public Mono<PlaybackQueue> save(PlaybackQueue queue) {
        PlaybackQueueEntity entity = mapper.toEntity(queue);
        return redisTemplate
                .opsForValue()
                .set(KEY_PREFIX + queue.getSessionId().value().toString(), entity)
                .thenReturn(queue);
    }

    @Override
    public Mono<Void> deleteBySessionId(SessionId sessionId) {
        return redisTemplate
                .opsForValue()
                .delete(KEY_PREFIX + sessionId.value().toString())
                .then();
    }
}

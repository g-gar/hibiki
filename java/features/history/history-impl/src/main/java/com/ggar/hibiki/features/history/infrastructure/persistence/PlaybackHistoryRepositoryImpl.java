package com.ggar.hibiki.features.history.infrastructure.persistence;

import com.ggar.hibiki.features.history.infrastructure.persistence.mapper.PlaybackHistoryMapper;
import com.ggar.hibiki.features.history.infrastructure.persistence.repository.Neo4jPlaybackHistoryRepository;
import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import com.ggar.hibiki.features.history.port.PlaybackHistoryRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link PlaybackHistoryRepository} using Neo4j.
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class PlaybackHistoryRepositoryImpl implements PlaybackHistoryRepository {

    private final Neo4jPlaybackHistoryRepository repository;
    private final PlaybackHistoryMapper mapper;

    @Override
    public Mono<PlaybackHistoryEntry> save(PlaybackHistoryEntry entry) {
        return repository.save(mapper.toEntity(entry)).map(mapper::toDomain);
    }

    @Override
    public Flux<PlaybackHistoryEntry> findByUserId(UUID userId) {
        return repository.findByUserIdOrderByPlayedAtDesc(userId.toString()).map(mapper::toDomain);
    }
}

package com.ggar.hibiki.features.history.infrastructure.persistence.repository;

import com.ggar.hibiki.features.history.infrastructure.persistence.entity.PlaybackHistoryEntity;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import reactor.core.publisher.Flux;

public interface Neo4jPlaybackHistoryRepository extends ReactiveNeo4jRepository<PlaybackHistoryEntity, String> {
    Flux<PlaybackHistoryEntity> findByUserIdOrderByPlayedAtDesc(String userId);
}

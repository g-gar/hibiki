package com.ggar.hibiki.features.ingestion.infrastructure.persistence.adapter;

import com.ggar.hibiki.features.ingestion.infrastructure.persistence.mapper.MediaMapper;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.repository.ReactiveNeo4jMediaRepository;
import com.ggar.hibiki.features.ingestion.model.Media;
import com.ggar.hibiki.features.ingestion.model.MediaId;
import com.ggar.hibiki.features.ingestion.port.MediaRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class MediaPersistenceAdapter implements MediaRepository {

    private final ReactiveNeo4jMediaRepository repository;
    private final MediaMapper mapper;

    public MediaPersistenceAdapter(ReactiveNeo4jMediaRepository repository, MediaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Media> save(Media media) {
        return Mono.just(media).map(mapper::toEntity).flatMap(repository::save).map(mapper::toDomain);
    }

    @Override
    public Mono<Media> findById(MediaId id) {
        return repository.findById(id.getId()).map(mapper::toDomain);
    }
}

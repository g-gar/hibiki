package com.ggar.hibiki.features.ingestion.infrastructure.persistence.adapter;

import com.ggar.hibiki.features.ingestion.domain.Media;
import com.ggar.hibiki.features.ingestion.domain.port.MediaRepository;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.mapper.MediaMapper;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.repository.ReactiveNeo4jMediaRepository;
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
        return Mono.just(media)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDomain);
    }
}

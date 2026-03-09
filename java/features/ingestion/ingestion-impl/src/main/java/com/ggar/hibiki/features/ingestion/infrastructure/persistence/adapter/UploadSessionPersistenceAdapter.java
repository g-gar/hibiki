package com.ggar.hibiki.features.ingestion.infrastructure.persistence.adapter;

import com.ggar.hibiki.features.ingestion.infrastructure.persistence.mapper.MediaMapper;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.repository.ReactiveNeo4jUploadSessionRepository;
import com.ggar.hibiki.features.ingestion.model.UploadSession;
import com.ggar.hibiki.features.ingestion.port.UploadSessionRepository;
import java.util.UUID;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class UploadSessionPersistenceAdapter implements UploadSessionRepository {

    private final ReactiveNeo4jUploadSessionRepository repository;
    private final MediaMapper mapper;

    public UploadSessionPersistenceAdapter(ReactiveNeo4jUploadSessionRepository repository, MediaMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<UploadSession> save(UploadSession session) {
        return Mono.just(session)
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDomain);
    }

    @Override
    public Mono<UploadSession> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return repository.deleteById(id);
    }
}

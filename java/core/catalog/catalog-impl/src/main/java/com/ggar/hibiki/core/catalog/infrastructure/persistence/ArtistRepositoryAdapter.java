package com.ggar.hibiki.core.catalog.infrastructure.persistence;

import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.catalog.persistence.mapper.ArtistMapper;
import com.ggar.hibiki.core.catalog.persistence.repository.Neo4jArtistRepository;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ArtistRepositoryAdapter implements ArtistRepository {

    private final Neo4jArtistRepository neo4jArtistRepository;
    private final ArtistMapper artistMapper;

    @Override
    public Mono<Artist> save(Artist artist) {
        return neo4jArtistRepository.save(artistMapper.toEntity(artist)).map(artistMapper::toDomain);
    }

    @Override
    public Mono<Artist> findById(UUID id) {
        return neo4jArtistRepository.findById(id).map(artistMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return neo4jArtistRepository.existsById(id);
    }

    @Override
    public Mono<Long> count() {
        return neo4jArtistRepository.count();
    }

    @Override
    public Mono<Artist> findByName(String name) {
        return neo4jArtistRepository.findByNameIgnoreCase(name).map(artistMapper::toDomain);
    }

    @Override
    public Mono<Artist> findByIsni(String isni) {
        return neo4jArtistRepository.findByIsni(isni).map(artistMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return neo4jArtistRepository.deleteById(id);
    }
}

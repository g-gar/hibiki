package com.ggar.hibiki.core.catalog.infrastructure.persistence;

import com.ggar.hibiki.core.catalog.model.Song;
import com.ggar.hibiki.core.catalog.persistence.mapper.SongMapper;
import com.ggar.hibiki.core.catalog.persistence.repository.Neo4jSongRepository;
import com.ggar.hibiki.core.catalog.port.SongRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class SongRepositoryAdapter implements SongRepository {

    private final Neo4jSongRepository neo4jSongRepository;
    private final SongMapper songMapper;

    @Override
    public Mono<Song> findById(UUID id) {
        return neo4jSongRepository.findById(id).map(songMapper::toDomain);
    }

    @Override
    public Mono<Song> save(Song song) {
        return neo4jSongRepository.save(songMapper.toEntity(song)).map(songMapper::toDomain);
    }

    @Override
    public Mono<Song> findByIsrc(String isrc) {
        return neo4jSongRepository.findByIsrc(isrc).map(songMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return neo4jSongRepository.existsById(id);
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return neo4jSongRepository.deleteById(id);
    }

    @Override
    public Mono<Long> count() {
        return neo4jSongRepository.count();
    }
}

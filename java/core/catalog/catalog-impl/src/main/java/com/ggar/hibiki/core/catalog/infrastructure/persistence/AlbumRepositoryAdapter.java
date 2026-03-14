package com.ggar.hibiki.core.catalog.infrastructure.persistence;

import com.ggar.hibiki.core.catalog.model.Album;
import com.ggar.hibiki.core.catalog.persistence.mapper.AlbumMapper;
import com.ggar.hibiki.core.catalog.persistence.repository.Neo4jAlbumRepository;
import com.ggar.hibiki.core.catalog.port.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AlbumRepositoryAdapter implements AlbumRepository {

    private final Neo4jAlbumRepository neo4jAlbumRepository;
    private final AlbumMapper albumMapper;

    @Override
    public Mono<Album> save(Album album) {
        return neo4jAlbumRepository.save(albumMapper.toEntity(album)).map(albumMapper::toDomain);
    }

    @Override
    public Mono<Boolean> existsById(UUID id) {
        return neo4jAlbumRepository.existsById(id);
    }

    @Override
    public Mono<Album> findById(UUID id) {
        return neo4jAlbumRepository.findById(id).map(albumMapper::toDomain);
    }

    @Override
    public Mono<Album> findByTitle(String title) {
        return neo4jAlbumRepository.findByTitleIgnoreCase(title).map(albumMapper::toDomain);
    }

    @Override
    public Mono<Album> findByTitleAndArtist(String title, String artistName) {
        return neo4jAlbumRepository
                .findByTitleIgnoreCaseAndArtistNameIgnoreCase(title, artistName)
                .map(albumMapper::toDomain);
    }

    @Override
    public Mono<Album> findByBarcode(String barcode) {
        return neo4jAlbumRepository.findByBarcode(barcode).map(albumMapper::toDomain);
    }

    @Override
    public Mono<Void> deleteById(UUID id) {
        return neo4jAlbumRepository.deleteById(id);
    }

    @Override
    public Mono<Void> deleteByArtistId(UUID artistId) {
        return neo4jAlbumRepository.deleteByArtistId(artistId);
    }

    @Override
    public Mono<Long> count() {
        return neo4jAlbumRepository.count();
    }
}

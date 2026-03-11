package com.ggar.hibiki.core.catalog.usecase.handler.query;

import com.ggar.hibiki.core.catalog.dto.FindArtistQuery;
import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.catalog.persistence.mapper.ArtistMapper;
import com.ggar.hibiki.core.catalog.persistence.repository.ArtistRepository;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FindArtistQueryHandler implements QueryHandler<FindArtistQuery, Artist> {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    @Override
    public Mono<Artist> handle(FindArtistQuery query) {
        if (query.getId() != null) {
            return artistRepository.findById(query.getId()).map(artistMapper::toDomain);
        } else if (query.getIsni() != null) {
            return artistRepository.findByIsni(query.getIsni()).map(artistMapper::toDomain);
        } else if (query.getName() != null) {
            return artistRepository.findByNameIgnoreCase(query.getName()).map(artistMapper::toDomain);
        }
        return Mono.empty();
    }
}

package com.ggar.hibiki.core.catalog.usecase.handler.query;

import com.ggar.hibiki.core.catalog.dto.ArtistDto;
import com.ggar.hibiki.core.catalog.dto.FindArtistQuery;
import com.ggar.hibiki.core.catalog.persistence.mapper.ArtistMapper;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FindArtistQueryHandler implements QueryHandler<FindArtistQuery, ArtistDto> {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    @Override
    public Mono<ArtistDto> handle(FindArtistQuery query) {
        if (query.getId() != null) {
            return artistRepository.findById(query.getId()).map(artistMapper::toDto);
        } else if (query.getIsni() != null) {
            return artistRepository.findByIsni(query.getIsni()).map(artistMapper::toDto);
        } else if (query.getName() != null) {
            return artistRepository.findByName(query.getName()).map(artistMapper::toDto);
        }
        return Mono.empty();
    }
}

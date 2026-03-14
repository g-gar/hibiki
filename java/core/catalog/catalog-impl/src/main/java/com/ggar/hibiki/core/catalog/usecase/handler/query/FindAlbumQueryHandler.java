package com.ggar.hibiki.core.catalog.usecase.handler.query;

import com.ggar.hibiki.core.catalog.dto.AlbumDto;
import com.ggar.hibiki.core.catalog.dto.FindAlbumQuery;
import com.ggar.hibiki.core.catalog.persistence.mapper.AlbumMapper;
import com.ggar.hibiki.core.catalog.port.AlbumRepository;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FindAlbumQueryHandler implements QueryHandler<FindAlbumQuery, AlbumDto> {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    @Override
    public Mono<AlbumDto> handle(FindAlbumQuery query) {
        if (query.getId() != null) {
            return albumRepository.findById(query.getId()).map(albumMapper::toDto);
        } else if (query.getBarcode() != null) {
            return albumRepository.findByBarcode(query.getBarcode()).map(albumMapper::toDto);
        } else if (query.getTitle() != null) {
            return albumRepository.findByTitle(query.getTitle()).map(albumMapper::toDto);
        }
        return Mono.empty();
    }
}

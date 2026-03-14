package com.ggar.hibiki.core.catalog.usecase.handler.query;

import com.ggar.hibiki.core.catalog.dto.FindSongQuery;
import com.ggar.hibiki.core.catalog.dto.SongDto;
import com.ggar.hibiki.core.catalog.persistence.mapper.SongMapper;
import com.ggar.hibiki.core.catalog.port.SongRepository;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FindSongQueryHandler implements QueryHandler<FindSongQuery, SongDto> {

    private final SongRepository songRepository;
    private final SongMapper songMapper;

    @Override
    public Mono<SongDto> handle(FindSongQuery query) {
        if (query.getId() != null) {
            return songRepository.findById(query.getId()).map(songMapper::toDto);
        } else if (query.getIsrc() != null) {
            return songRepository.findByIsrc(query.getIsrc()).map(songMapper::toDto);
        }
        return Mono.empty();
    }
}

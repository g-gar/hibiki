package com.ggar.hibiki.core.catalog.handler.query;

import com.ggar.hibiki.core.catalog.model.Song;
import com.ggar.hibiki.core.catalog.port.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link FindSongQueryHandler}.
 * This service handles querying for songs in the catalog by ID.
 */
@Service
@RequiredArgsConstructor
public class FindSongQueryHandlerImpl implements FindSongQueryHandler {

    private final SongRepository songRepository;

    /**
     * Handles the lookup of a song by ID.
     *
     * @param find The query containing the search criteria.
     * @return A {@link Mono} emitting the found {@link Song}, or empty if not found.
     */
    @Override
    public Mono<Song> handle(Find find) {
        return songRepository.findById(find.id());
    }
}

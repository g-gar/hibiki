package com.ggar.hibiki.core.catalog.handler.query;

import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link FindArtistQueryHandler}.
 * This service handles querying for artists in the catalog by ID, ISNI, or name.
 */
@Service
@RequiredArgsConstructor
public class FindArtistQueryHandlerImpl implements FindArtistQueryHandler {

    private final ArtistRepository artistRepository;

    /**
     * Handles the lookup of an artist.
     * Searches by ID, ISNI, or name in that order of priority.
     *
     * @param find The query containing the search criteria.
     * @return A {@link Mono} emitting the found {@link Artist}, or empty if not found.
     */
    @Override
    public Mono<Artist> handle(Find find) {
        if (find.id() != null) {
            return artistRepository.findById(find.id());
        } else if (find.isni() != null) {
            return artistRepository.findByIsni(find.isni());
        } else if (find.name() != null) {
            return artistRepository.findByName(find.name());
        }
        return Mono.empty();
    }
}

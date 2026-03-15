package com.ggar.hibiki.core.catalog.handler.query;

import com.ggar.hibiki.core.catalog.model.Album;
import com.ggar.hibiki.core.catalog.port.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link FindAlbumQueryHandler}.
 * This service handles querying for albums in the catalog by ID, barcode, or title.
 */
@Service
@RequiredArgsConstructor
public class FindAlbumQueryHandlerImpl implements FindAlbumQueryHandler {

    private final AlbumRepository albumRepository;

    /**
     * Handles the lookup of an album.
     * Searches by ID, barcode, or title in that order of priority.
     *
     * @param find The query containing the search criteria.
     * @return A {@link Mono} emitting the found {@link Album}, or empty if not found.
     */
    @Override
    public Mono<Album> handle(Find find) {
        if (find.id() != null) {
            return albumRepository.findById(find.id());
        } else if (find.barcode() != null) {
            return albumRepository.findByBarcode(find.barcode());
        } else if (find.title() != null) {
            return albumRepository.findByTitle(find.title());
        }
        return Mono.empty();
    }
}

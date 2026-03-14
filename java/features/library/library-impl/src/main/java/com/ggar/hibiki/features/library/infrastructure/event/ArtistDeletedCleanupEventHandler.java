package com.ggar.hibiki.features.library.infrastructure.event;

import com.ggar.hibiki.core.catalog.event.ArtistDeletedEvent;
import com.ggar.hibiki.core.shared.event.EventHandler;
import com.ggar.hibiki.features.library.port.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Event handler that cleanup library entries when an artist is deleted from Catalog.
 */
@Component
@RequiredArgsConstructor
public class ArtistDeletedCleanupEventHandler implements EventHandler<ArtistDeletedEvent> {

    private final LibraryRepository libraryRepository;

    @Override
    public Mono<Void> handle(ArtistDeletedEvent event) {
        return libraryRepository.removeByArtistId(event.getArtistId());
    }
}

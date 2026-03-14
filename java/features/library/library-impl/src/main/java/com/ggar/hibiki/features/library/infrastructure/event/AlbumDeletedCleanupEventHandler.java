package com.ggar.hibiki.features.library.infrastructure.event;

import com.ggar.hibiki.core.catalog.event.AlbumDeletedEvent;
import com.ggar.hibiki.core.shared.event.EventHandler;
import com.ggar.hibiki.features.library.port.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Event handler that cleanup library entries when an album is deleted from Catalog.
 */
@Component
@RequiredArgsConstructor
public class AlbumDeletedCleanupEventHandler implements EventHandler<AlbumDeletedEvent> {

    private final LibraryRepository libraryRepository;

    @Override
    public Mono<Void> handle(AlbumDeletedEvent event) {
        return libraryRepository.removeByAlbumId(event.getAlbumId());
    }
}

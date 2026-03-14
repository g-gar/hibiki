package com.ggar.hibiki.features.library.infrastructure.event;

import com.ggar.hibiki.core.catalog.event.SongDeletedEvent;
import com.ggar.hibiki.core.shared.event.EventHandler;
import com.ggar.hibiki.features.library.port.LibraryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Event handler that cleanup library entries when a song is deleted from Catalog.
 */
@Component
@RequiredArgsConstructor
public class SongDeletedCleanupEventHandler implements EventHandler<SongDeletedEvent> {

    private final LibraryRepository libraryRepository;

    @Override
    public Mono<Void> handle(SongDeletedEvent event) {
        return libraryRepository.removeBySongId(event.getSongId());
    }
}

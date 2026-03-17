package com.ggar.hibiki.features.library.infrastructure.event;

import com.ggar.hibiki.core.catalog.handler.command.DeleteAlbumCommandHandler;
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
public class AlbumDeletedCleanupEventHandler implements EventHandler<DeleteAlbumCommandHandler.Deleted> {

    private final LibraryRepository libraryRepository;

    @Override
    public Mono<Void> handle(DeleteAlbumCommandHandler.Deleted event) {
        return libraryRepository.removeByAlbumId(event.albumId());
    }
}

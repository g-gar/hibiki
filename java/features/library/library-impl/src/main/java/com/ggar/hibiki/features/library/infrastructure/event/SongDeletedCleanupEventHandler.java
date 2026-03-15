package com.ggar.hibiki.features.library.infrastructure.event;

import com.ggar.hibiki.core.catalog.handler.command.DeleteSongCommandHandler;
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
public class SongDeletedCleanupEventHandler implements EventHandler<DeleteSongCommandHandler.Deleted> {

    private final LibraryRepository libraryRepository;

    @Override
    public Mono<Void> handle(DeleteSongCommandHandler.Deleted event) {
        return libraryRepository.removeBySongId(event.songId());
    }
}

package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.port.AlbumRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link DeleteAlbumCommandHandler}.
 * This service handles the deletion of albums from the catalog.
 * Publishes an {@link DeleteAlbumCommandHandler.DeletedEvent} before deletion.
 */
@Service
@RequiredArgsConstructor
public class DeleteAlbumCommandHandlerImpl implements DeleteAlbumCommandHandler {

    private final AlbumRepository albumRepository;
    private final EventBus eventBus;

    /**
     * Handles the deletion of an album.
     * Publishes an {@link DeleteAlbumCommandHandler.Deleted} then deletes the album.
     *
     * @param delete The command containing the ID of the album to delete.
     * @return A {@link Mono} that completes when the album is deleted.
     */
    @Override
    public Mono<Void> handle(Delete delete) {
        return albumRepository.findById(delete.id()).flatMap(album -> eventBus.publish(
                        new Deleted(album.getId(), album.getTitle()))
                .then(albumRepository.deleteById(album.getId())));
    }
}

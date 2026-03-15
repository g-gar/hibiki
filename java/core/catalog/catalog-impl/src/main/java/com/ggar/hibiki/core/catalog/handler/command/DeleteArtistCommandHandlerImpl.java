package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.port.AlbumRepository;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link DeleteArtistCommandHandler}.
 * This service handles the deletion of artists and propagates the deletion to associated albums.
 */
@Service
@RequiredArgsConstructor
public class DeleteArtistCommandHandlerImpl implements DeleteArtistCommandHandler {

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final EventBus eventBus;

    /**
     * Handles the deletion of an artist.
     * Publishes an {@link DeleteArtistCommandHandler.Deleted} before deleting associated albums and the artist itself.
     *
     * @param delete The command containing the ID of the artist to delete.
     * @return A {@link Mono} that completes when the artist and its associations are deleted.
     */
    @Override
    public Mono<Void> handle(Delete delete) {
        return artistRepository.findById(delete.id()).flatMap(artist -> {
            Deleted event = new Deleted(artist.getId(), artist.getName());

            return eventBus.publish(event)
                    .then(albumRepository.deleteByArtistId(artist.getId()))
                    .then(artistRepository.deleteById(artist.getId()));
        });
    }
}

package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.model.Album;
import com.ggar.hibiki.core.catalog.port.AlbumRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link UpdateAlbumCommandHandler}.
 * This service handles updates to album information in the catalog.
 */
@Service
@RequiredArgsConstructor
public class UpdateAlbumCommandHandlerImpl implements UpdateAlbumCommandHandler {

    private final AlbumRepository albumRepository;
    private final EventBus eventBus;

    /**
     * Handles the update of an existing album.
     * Updates fields such as title, release year, and barcode if they are provided in the command.
     * Publishes an {@link UpdateAlbumCommandHandler.Updated} after a successful update.
     *
     * @param update The command containing the album update details.
     * @return A {@link Mono} emitting the updated {@link Album}.
     */
    @Override
    public Mono<Album> handle(Update update) {
        return albumRepository.findById(update.id()).flatMap(album -> {
            if (update.title() != null) album.setTitle(update.title());
            if (update.releaseYear() != null) album.setReleaseYear(update.releaseYear());
            if (update.barcode() != null) album.setBarcode(update.barcode());
            return albumRepository.save(album).flatMap(saved -> eventBus.publish(
                            new Updated(saved.getId(), saved.getTitle()))
                    .thenReturn(saved));
        });
    }
}

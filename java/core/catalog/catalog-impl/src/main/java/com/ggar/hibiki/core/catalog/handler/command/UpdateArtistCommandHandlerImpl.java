package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link UpdateArtistCommandHandler}.
 * This service handles updates to artist information in the catalog.
 */
@Service
@RequiredArgsConstructor
public class UpdateArtistCommandHandlerImpl implements UpdateArtistCommandHandler {

    private final ArtistRepository artistRepository;
    private final EventBus eventBus;

    /**
     * Handles the update of an existing artist.
     * Updates fields such as name and ISNI if they are provided in the command.
     * Publishes an {@link UpdateArtistCommandHandler.Updated} after a successful update.
     *
     * @param update The command containing the artist update details.
     * @return A {@link Mono} emitting the updated {@link Artist}.
     */
    @Override
    public Mono<Artist> handle(Update update) {
        return artistRepository.findById(update.id()).flatMap(artist -> {
            if (update.name() != null) artist.setName(update.name());
            if (update.isni() != null) artist.setIsni(update.isni());
            return artistRepository.save(artist).flatMap(saved -> eventBus.publish(
                            new Updated(saved.getId(), saved.getName()))
                    .thenReturn(saved));
        });
    }
}

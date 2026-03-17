package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link CreateArtistCommandHandler}.
 * This service handles the creation of artists within the catalog system.
 */
@Service
@RequiredArgsConstructor
public class CreateArtistCommandHandlerImpl implements CreateArtistCommandHandler {

    private final ArtistRepository artistRepository;
    private final EventBus eventBus;

    /**
     * Handles the creation of an artist.
     * If an artist with the same name already exists, it returns the existing one.
     * Otherwise, it creates and saves a new artist and publishes an {@link CreateArtistCommandHandler.Created}.
     *
     * @param create The command containing the artist creation details.
     * @return A {@link Mono} emitting the created or existing {@link Artist}.
     */
    @Override
    public Mono<Artist> handle(Create create) {
        return artistRepository.findByName(create.name()).switchIfEmpty(Mono.defer(() -> {
            Artist artist = Artist.builder().name(create.name()).build();
            return artistRepository.save(artist).flatMap(saved -> eventBus.publish(
                            new Created(saved.getId(), saved.getName()))
                    .thenReturn(saved));
        }));
    }
}

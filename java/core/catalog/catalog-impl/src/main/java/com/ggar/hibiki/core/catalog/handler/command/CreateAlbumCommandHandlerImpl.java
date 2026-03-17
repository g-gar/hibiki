package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.model.Album;
import com.ggar.hibiki.core.catalog.port.AlbumRepository;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link CreateAlbumCommandHandler}.
 * This service handles the creation of albums within the catalog, ensuring they are linked to an artist.
 * Publishes an {@link CreatedEvent} upon successful creation.
 */
@Service
@RequiredArgsConstructor
public class CreateAlbumCommandHandlerImpl implements CreateAlbumCommandHandler {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final EventBus eventBus;

    /**
     * Handles the creation of an album.
     * If an album with the same title and artist already exists, it returns the existing one.
     * Otherwise, it finds the artist, creates a new album, and publishes an event.
     *
     * @param create The command containing the album creation details.
     * @return A {@link Mono} emitting the created or existing {@link Album}.
     */
    @Override
    public Mono<Album> handle(Create create) {
        return albumRepository
                .findByTitleAndArtist(create.title(), create.artistName())
                .switchIfEmpty(Mono.defer(
                        () -> artistRepository.findByName(create.artistName()).flatMap(artist -> {
                            Album album = Album.builder()
                                    .title(create.title())
                                    .releaseYear(create.releaseYear())
                                    .artist(artist)
                                    .build();
                            return albumRepository.save(album).flatMap(saved -> eventBus.publish(
                                            new Created(saved.getId(), saved.getTitle(), artist.getName()))
                                    .thenReturn(saved));
                        })));
    }
}

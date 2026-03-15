package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.model.Song;
import com.ggar.hibiki.core.catalog.port.AlbumRepository;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.core.catalog.port.SongRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link CreateSongCommandHandler}.
 * This service handles the creation of songs in the catalog, including resolving their album and artists.
 * Publishes a {@link CreatedEvent} upon successful creation.
 */
@Service
@RequiredArgsConstructor
public class CreateSongCommandHandlerImpl implements CreateSongCommandHandler {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final EventBus eventBus;

    /**
     * Handles the creation of a song.
     * If a song with the same ISRC already exists, it returns the existing one.
     * Otherwise, it creates a new song and resolves its relationships.
     *
     * @param create The command containing the song creation details.
     * @return A {@link Mono} emitting the created or existing {@link Song}.
     */
    @Override
    public Mono<Song> handle(Create create) {
        return songRepository.findByIsrc(create.isrc()).switchIfEmpty(Mono.defer(() -> createNewSong(create)));
    }

    /**
     * Creates a new song, resolving its album and artists.
     *
     * @param create The creation command.
     * @return A {@link Mono} emitting the saved {@link Song}.
     */
    private Mono<Song> createNewSong(Create create) {
        Song song = Song.builder()
                .title(create.title())
                .filePath(create.filePath())
                .durationMs(create.durationMs())
                .trackNumber(create.trackNumber())
                .isrc(create.isrc())
                .build();

        Mono<Song> withAlbum = (create.albumTitle() != null
                        && create.artistNames() != null
                        && !create.artistNames().isEmpty())
                ? albumRepository
                        .findByTitleAndArtist(
                                create.albumTitle(), create.artistNames().get(0))
                        .doOnNext(song::setAlbum)
                        .thenReturn(song)
                : Mono.just(song);

        return withAlbum.flatMap(s -> Flux.fromIterable(
                        create.artistNames() != null ? create.artistNames() : Collections.<String>emptyList())
                .flatMap(artistRepository::findByName)
                .collectList()
                .doOnNext(song::setArtists)
                .then(songRepository.save(song))
                .flatMap(saved -> eventBus.publish(new Created(saved.getId(), saved.getTitle(), saved.getIsrc()))
                        .thenReturn(saved)));
    }
}

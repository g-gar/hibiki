package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.port.SongRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link DeleteSongCommandHandler}.
 * This service handles the deletion of songs from the catalog.
 * Publishes a {@link DeletedEvent} before deletion.
 */
@Service
@RequiredArgsConstructor
public class DeleteSongCommandHandlerImpl implements DeleteSongCommandHandler {

    private final SongRepository songRepository;
    private final EventBus eventBus;

    /**
     * Handles the deletion of a song.
     * Publishes a {@link DeleteSongCommandHandler.Deleted} then deletes the song.
     *
     * @param delete The command containing the ID of the song to delete.
     * @return A {@link Mono} that completes when the song is deleted.
     */
    @Override
    public Mono<Void> handle(Delete delete) {
        return songRepository.findById(delete.id()).flatMap(song -> eventBus.publish(
                        new Deleted(song.getId(), song.getTitle()))
                .then(songRepository.deleteById(song.getId())));
    }
}

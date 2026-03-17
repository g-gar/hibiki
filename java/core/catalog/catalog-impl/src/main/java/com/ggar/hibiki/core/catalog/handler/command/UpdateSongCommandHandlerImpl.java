package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.model.Song;
import com.ggar.hibiki.core.catalog.port.SongRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link UpdateSongCommandHandler}.
 * This service handles updates to song information in the catalog.
 */
@Service
@RequiredArgsConstructor
public class UpdateSongCommandHandlerImpl implements UpdateSongCommandHandler {

    private final SongRepository songRepository;
    private final EventBus eventBus;

    /**
     * Handles the update of an existing song.
     * Updates fields such as title, file path, duration, track number, and ISRC if they are provided in the command.
     * Publishes an {@link UpdateSongCommandHandler.Updated} after a successful update.
     *
     * @param update The command containing the song update details.
     * @return A {@link Mono} emitting the updated {@link Song}.
     */
    @Override
    public Mono<Song> handle(Update update) {
        return songRepository.findById(update.id()).flatMap(song -> {
            if (update.title() != null) song.setTitle(update.title());
            if (update.filePath() != null) song.setFilePath(update.filePath());
            if (update.durationMs() != null) song.setDurationMs(update.durationMs());
            if (update.trackNumber() != null) song.setTrackNumber(update.trackNumber());
            if (update.isrc() != null) song.setIsrc(update.isrc());
            return songRepository.save(song).flatMap(saved -> eventBus.publish(
                            new Updated(saved.getId(), saved.getTitle()))
                    .thenReturn(saved));
        });
    }
}

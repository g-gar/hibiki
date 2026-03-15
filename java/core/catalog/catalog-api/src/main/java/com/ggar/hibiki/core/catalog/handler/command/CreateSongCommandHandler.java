package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.model.Song;
import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.List;
import java.util.UUID;

/**
 * Interface for the command handler that creates a new Song in the catalog.
 */
public interface CreateSongCommandHandler extends CommandHandler<CreateSongCommandHandler.Create, Song> {

    /**
     * Data needed to create a new song.
     */
    record Create(
            String title,
            String filePath,
            Long durationMs,
            Integer trackNumber,
            String isrc,
            String albumTitle,
            List<String> artistNames)
            implements Command<Song> {}

    /**
     * Event published when a new song is successfully created.
     */
    record Created(UUID songId, String title, String isrc) implements DomainEvent {}
}

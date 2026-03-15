package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.model.Song;
import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/**
 * Interface for the command handler that updates an existing Song in the catalog.
 */
public interface UpdateSongCommandHandler extends CommandHandler<UpdateSongCommandHandler.Update, Song> {

    /**
     * Data needed to update a song.
     */
    record Update(UUID id, String title, String filePath, Long durationMs, Integer trackNumber, String isrc)
            implements Command<Song> {}

    /**
     * Event published when a song's information is successfully updated.
     */
    record Updated(UUID songId, String title) implements DomainEvent {}
}

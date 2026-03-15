package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/**
 * Interface for the command handler that deletes a Song and its associated data from the catalog.
 */
public interface DeleteSongCommandHandler extends CommandHandler<DeleteSongCommandHandler.Delete, Void> {

    /**
     * Data needed to delete a song.
     */
    record Delete(UUID id) implements Command<Void> {}

    /**
     * Event published when a song is successfully deleted.
     */
    record Deleted(UUID songId, String title) implements DomainEvent {}
}

package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/**
 * Interface for the command handler that deletes an Album and its associated data from the catalog.
 */
public interface DeleteAlbumCommandHandler extends CommandHandler<DeleteAlbumCommandHandler.Delete, Void> {

    /**
     * Data needed to delete an album.
     */
    record Delete(UUID id) implements Command<Void> {}

    /**
     * Event published when an album is successfully deleted.
     */
    record Deleted(UUID albumId, String title) implements DomainEvent {}
}

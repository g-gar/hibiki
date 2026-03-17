package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/**
 * Interface for the command handler that deletes an Artist and its associated data from the catalog.
 */
public interface DeleteArtistCommandHandler extends CommandHandler<DeleteArtistCommandHandler.Delete, Void> {

    /**
     * Data needed to delete an artist.
     */
    record Delete(UUID id) implements Command<Void> {}

    /**
     * Event published when an artist is successfully deleted.
     */
    record Deleted(UUID artistId, String name) implements DomainEvent {}
}

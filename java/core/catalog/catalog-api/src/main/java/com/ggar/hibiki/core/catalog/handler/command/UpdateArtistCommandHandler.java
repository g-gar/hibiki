package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/**
 * Interface for the command handler that updates an existing Artist in the catalog.
 */
public interface UpdateArtistCommandHandler extends CommandHandler<UpdateArtistCommandHandler.Update, Artist> {

    /**
     * Data needed to update an artist.
     */
    record Update(UUID id, String name, String isni) implements Command<Artist> {}

    /**
     * Event published when an artist's information is successfully updated.
     */
    record Updated(UUID artistId, String name) implements DomainEvent {}
}

package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/**
 * Interface for the command handler that creates a new Artist in the catalog.
 * This handler ensures that the artist is uniquely created by name.
 */
public interface CreateArtistCommandHandler extends CommandHandler<CreateArtistCommandHandler.Create, Artist> {

    /**
     * Data needed to create a new artist.
     */
    record Create(String name) implements Command<Artist> {}

    /**
     * Event published when a new artist is successfully created.
     */
    record Created(UUID artistId, String name) implements DomainEvent {}
}

package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.model.Album;
import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/**
 * Interface for the command handler that creates a new Album in the catalog.
 */
public interface CreateAlbumCommandHandler extends CommandHandler<CreateAlbumCommandHandler.Create, Album> {

    /**
     * Data needed to create a new album.
     */
    record Create(String title, Integer releaseYear, String artistName) implements Command<Album> {}

    /**
     * Event published when a new album is successfully created.
     */
    record Created(UUID albumId, String title, String artistName) implements DomainEvent {}
}

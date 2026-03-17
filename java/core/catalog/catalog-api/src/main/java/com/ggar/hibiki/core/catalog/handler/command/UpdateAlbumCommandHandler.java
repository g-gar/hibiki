package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.model.Album;
import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/**
 * Interface for the command handler that updates an existing Album in the catalog.
 */
public interface UpdateAlbumCommandHandler extends CommandHandler<UpdateAlbumCommandHandler.Update, Album> {

    /**
     * Data needed to update an album.
     */
    record Update(UUID id, String title, Integer releaseYear, String barcode) implements Command<Album> {}

    /**
     * Event published when an album's information is successfully updated.
     */
    record Updated(UUID albumId, String title) implements DomainEvent {}
}

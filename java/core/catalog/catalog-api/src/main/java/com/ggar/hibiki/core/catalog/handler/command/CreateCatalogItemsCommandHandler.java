package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.packages.id3v2.model.Id3v2Tag;
import java.util.UUID;

/**
 * Interface for the command handler that creates artists, albums, and songs in the catalog
 * by parsing standard ID3 frames.
 */
public interface CreateCatalogItemsCommandHandler
        extends CommandHandler<CreateCatalogItemsCommandHandler.Command, CreateCatalogItemsCommandHandler.Result> {

    /**
     * Data needed to create items from ID3 tag.
     */
    record Command(UUID mediaId, Id3v2Tag id3Tag) implements com.ggar.hibiki.core.shared.mediator.Command<Result> {}

    /**
     * Result of creating items in the catalog.
     */
    record Result(UUID songId, UUID albumId) {}
}

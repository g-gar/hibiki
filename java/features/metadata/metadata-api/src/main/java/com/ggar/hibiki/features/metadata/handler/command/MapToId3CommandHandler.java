package com.ggar.hibiki.features.metadata.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.metadata.model.Id3Result;
import com.ggar.hibiki.packages.id3v2.model.Id3v2Tag;
import com.ggar.hibiki.packages.musicbrainz.model.IsrcResponse;
import java.util.UUID;

/**
 * Contract for a handler that maps raw external metadata into a standard ID3v2 tag format.
 */
public interface MapToId3CommandHandler extends CommandHandler<MapToId3CommandHandler.MapMetadata, Id3Result> {

    /**
     * Command to map raw metadata into standard ID3v2 tags.
     */
    record MapMetadata(UUID mediaId, IsrcResponse rawMetadata) implements Command<Id3Result> {}

    /**
     * Event published when metadata is successfully mapped to ID3 tags.
     */
    record Mapped(UUID mediaId, Id3v2Tag tags) implements DomainEvent {}
}

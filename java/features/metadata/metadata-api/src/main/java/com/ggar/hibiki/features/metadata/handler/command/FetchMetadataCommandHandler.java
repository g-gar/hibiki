package com.ggar.hibiki.features.metadata.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.metadata.model.FetchMetadataResult;
import com.ggar.hibiki.packages.musicbrainz.model.IsrcResponse;
import java.util.UUID;

/**
 * Contract for a handler that fetches metadata from external sources using a fingerprint.
 */
public interface FetchMetadataCommandHandler
        extends CommandHandler<FetchMetadataCommandHandler.Fetch, FetchMetadataResult> {

    /**
     * Command to lookup metadata (e.g. from MusicBrainz) using a fingerprint.
     */
    record Fetch(UUID mediaId, String acoustId, String mimeType) implements Command<FetchMetadataResult> {}

    /**
     * Event published when metadata is successfully fetched from an external source.
     */
    record Fetched(UUID mediaId, IsrcResponse metadata) implements DomainEvent {}
}

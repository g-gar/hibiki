package com.ggar.hibiki.features.metadata.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.metadata.model.FingerprintId;
import com.ggar.hibiki.features.metadata.model.FingerprintResult;
import java.util.UUID;

/**
 * Contract for a handler that extracts a fingerprint from a media source.
 */
public interface ExtractFingerprintCommandHandler
        extends CommandHandler<ExtractFingerprintCommandHandler.Extract, FingerprintResult> {

    /**
     * Command to extract an AcoustID fingerprint from an audio file.
     */
    record Extract(UUID mediaId, String mimeType) implements Command<FingerprintResult> {}

    /**
     * Event published when a fingerprint is successfully extracted.
     */
    record Extracted(UUID mediaId, FingerprintId acoustId) implements DomainEvent {}
}

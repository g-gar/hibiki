package com.ggar.hibiki.features.ingestion.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.ingestion.model.UploadSession;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Cancels an active upload session. Returns the session in CANCELLED state
 * so the frontend can confirm the action without querying the database.
 */
@Value
@Builder
public class CancelUploadCommand implements Command<UploadSession> {
    UUID userId;
    UUID uploadSessionId;
}

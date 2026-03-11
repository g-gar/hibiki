package com.ggar.hibiki.features.ingestion.dto;

import com.ggar.hibiki.core.shared.mediator.Query;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * System query to retrieve an upload session strictly by its ID without requiring
 * an active user IdentityContext (e.g., used by Orchestrator webhooks).
 */
@Value
@Builder
public class GetUploadSessionByIdQuery implements Query<UploadSessionDto> {
    UUID uploadSessionId;
}

package com.ggar.hibiki.features.ingestion.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Completes an upload session, assembling all parts in S3 and triggering
 * the ingestion pipeline.
 */
@Value
@Builder
public class CompleteUploadCommand implements Command<UploadSessionDto> {
    UUID userId;
    UUID uploadSessionId;
    String mimeType;
    String contentHash;
}

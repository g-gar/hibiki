package com.ggar.hibiki.features.ingestion.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.ingestion.model.IdentityContext;
import com.ggar.hibiki.features.ingestion.model.UploadSession;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import lombok.Builder;
import lombok.Value;

/**
 * Completes an upload session, assembling all parts in S3 and triggering
 * the ingestion pipeline.
 */
@Value
@Builder
public class CompleteUploadCommand implements Command<UploadSession> {
    IdentityContext identityContext;
    UploadSessionId uploadSessionId;
    String mimeType;
    String contentHash;
}

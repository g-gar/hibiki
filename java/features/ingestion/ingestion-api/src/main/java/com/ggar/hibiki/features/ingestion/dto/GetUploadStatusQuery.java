package com.ggar.hibiki.features.ingestion.dto;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.features.ingestion.model.IdentityContext;
import com.ggar.hibiki.features.ingestion.model.UploadSession;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import lombok.Builder;
import lombok.Value;

/**
 * Queries the current status of an upload session.
 */
@Value
@Builder
public class GetUploadStatusQuery implements Query<UploadSession> {
    IdentityContext identityContext;
    UploadSessionId uploadSessionId;
}

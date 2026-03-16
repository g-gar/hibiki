package com.ggar.hibiki.features.ingestion.handler.query;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import com.ggar.hibiki.features.ingestion.dto.UploadSessionDto;
import java.util.UUID;

/**
 * Interface for the handler responsible for querying the current status of an upload session.
 */
public interface GetUploadStatusQueryHandler extends QueryHandler<GetUploadStatusQueryHandler.Get, UploadSessionDto> {

    /**
     * Query to retrieve the current status of an upload session.
     */
    record Get(UUID userId, UUID uploadSessionId) implements Query<UploadSessionDto> {}
}

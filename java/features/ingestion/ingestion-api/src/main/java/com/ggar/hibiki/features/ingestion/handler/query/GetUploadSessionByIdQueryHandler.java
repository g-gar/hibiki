package com.ggar.hibiki.features.ingestion.handler.query;

import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import com.ggar.hibiki.features.ingestion.model.UploadSession;
import java.util.UUID;

/**
 * Interface for the handler responsible for retrieving an upload session by its ID.
 */
public interface GetUploadSessionByIdQueryHandler
        extends QueryHandler<GetUploadSessionByIdQueryHandler.Get, UploadSession> {

    /**
     * Query to retrieve an upload session strictly by its ID.
     */
    record Get(UUID uploadSessionId) implements Query<UploadSession> {}
}

package com.ggar.hibiki.features.ingestion.service;

import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import com.ggar.hibiki.features.ingestion.dto.GetUploadSessionByIdQuery;
import com.ggar.hibiki.features.ingestion.model.UploadSession;

public interface GetUploadSessionByIdQueryHandler extends QueryHandler<GetUploadSessionByIdQuery, UploadSession> {}

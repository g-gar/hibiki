package com.ggar.hibiki.features.ingestion.service;

import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import com.ggar.hibiki.features.ingestion.dto.GetUploadStatusQuery;
import com.ggar.hibiki.features.ingestion.model.UploadSession;

public interface GetUploadStatusQueryHandler extends QueryHandler<GetUploadStatusQuery, UploadSession> {}

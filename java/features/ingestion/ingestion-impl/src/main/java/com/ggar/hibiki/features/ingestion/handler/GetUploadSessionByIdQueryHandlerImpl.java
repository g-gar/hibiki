package com.ggar.hibiki.features.ingestion.handler;

import com.ggar.hibiki.features.ingestion.dto.GetUploadSessionByIdQuery;
import com.ggar.hibiki.features.ingestion.model.UploadSession;
import com.ggar.hibiki.features.ingestion.port.UploadSessionRepository;
import com.ggar.hibiki.features.ingestion.service.GetUploadSessionByIdQueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUploadSessionByIdQueryHandlerImpl implements GetUploadSessionByIdQueryHandler {

    private final UploadSessionRepository uploadSessionRepository;

    @Override
    public Publisher<UploadSession> handle(GetUploadSessionByIdQuery query) {
        return uploadSessionRepository.findById(query.getUploadSessionId());
    }
}

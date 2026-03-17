package com.ggar.hibiki.features.ingestion.handler.query;

import com.ggar.hibiki.features.ingestion.model.UploadSession;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.ingestion.port.UploadSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUploadStatusQueryHandlerImpl implements GetUploadStatusQueryHandler {

    private final UploadSessionRepository uploadSessionRepository;

    @Override
    public Publisher<UploadSession> handle(GetUploadStatusQueryHandler.Get query) {
        return uploadSessionRepository.findById(UploadSessionId.of(query.uploadSessionId()));
    }
}

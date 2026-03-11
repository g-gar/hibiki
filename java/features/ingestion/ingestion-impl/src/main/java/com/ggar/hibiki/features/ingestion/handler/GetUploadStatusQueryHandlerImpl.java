package com.ggar.hibiki.features.ingestion.handler;

import com.ggar.hibiki.features.ingestion.dto.GetUploadStatusQuery;
import com.ggar.hibiki.features.ingestion.dto.UploadSessionDto;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.mapper.MediaMapper;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.ingestion.port.UploadSessionRepository;
import com.ggar.hibiki.features.ingestion.service.GetUploadStatusQueryHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GetUploadStatusQueryHandlerImpl implements GetUploadStatusQueryHandler {

    private final UploadSessionRepository uploadSessionRepository;
    private final MediaMapper mediaMapper;

    @Override
    public Publisher<UploadSessionDto> handle(GetUploadStatusQuery query) {
        return uploadSessionRepository
                .findById(UploadSessionId.of(query.getUploadSessionId()))
                .map(mediaMapper::toDto);
    }
}

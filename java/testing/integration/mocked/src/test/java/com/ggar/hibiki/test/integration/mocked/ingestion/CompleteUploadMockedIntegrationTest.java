package com.ggar.hibiki.test.integration.mocked.ingestion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ggar.hibiki.features.ingestion.dto.CompleteUploadCommand;
import com.ggar.hibiki.features.ingestion.dto.UploadSessionDto;
import com.ggar.hibiki.features.ingestion.handler.CompleteUploadCommandHandlerImpl;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.mapper.MediaMapper;
import com.ggar.hibiki.features.ingestion.model.UploadItem;
import com.ggar.hibiki.features.ingestion.model.UploadItemId;
import com.ggar.hibiki.features.ingestion.model.UploadSession;
import com.ggar.hibiki.features.ingestion.model.UploadSessionId;
import com.ggar.hibiki.features.ingestion.pipeline.IngestionPipeline;
import com.ggar.hibiki.features.ingestion.port.MediaRepository;
import com.ggar.hibiki.features.ingestion.port.MediaStorage;
import com.ggar.hibiki.features.ingestion.port.UploadSessionRepository;
import com.ggar.hibiki.test.support.CapturingEventBus;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class CompleteUploadMockedIntegrationTest {

    @Mock
    private UploadSessionRepository uploadSessionRepository;

    @Mock
    private MediaStorage mediaStorage;

    @Mock
    private MediaRepository mediaRepository;

    @Mock
    private IngestionPipeline ingestionPipeline;

    @Mock
    private MediaMapper mediaMapper;

    private final CapturingEventBus eventBus = new CapturingEventBus();
    private CompleteUploadCommandHandlerImpl handler;

    @BeforeEach
    void setup() {
        handler = new CompleteUploadCommandHandlerImpl(
                uploadSessionRepository, mediaStorage, mediaRepository, ingestionPipeline, mediaMapper, eventBus);
        eventBus.clear();
    }

    @Test
    @DisplayName("Scenario: successful upload completion")
    void shouldCompleteUpload() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        UploadSession session = UploadSession.builder()
                .id(UploadSessionId.of(sessionId))
                .items(List.of(UploadItem.builder()
                        .id(UploadItemId.of(itemId))
                        .originalFilename("test.mp3")
                        .expectedSize(100L)
                        .build()))
                .build();

        when(uploadSessionRepository.findById(any())).thenReturn(Mono.just(session));
        when(mediaStorage.completeMultipartUpload(any(), any(), any())).thenReturn(Mono.empty());
        when(mediaRepository.save(any()))
                .thenReturn(Mono.just(mock(com.ggar.hibiki.features.ingestion.model.Media.class)));
        when(ingestionPipeline.execute(any()))
                .thenReturn(Mono.just(mock(com.ggar.hibiki.features.ingestion.pipeline.IngestionContext.class)));
        when(uploadSessionRepository.save(any(com.ggar.hibiki.features.ingestion.model.UploadSession.class)))
                .thenReturn(Mono.just(session));
        when(mediaMapper.toDto(any(com.ggar.hibiki.features.ingestion.model.UploadSession.class)))
                .thenReturn(UploadSessionDto.builder().build());

        CompleteUploadCommand command = CompleteUploadCommand.builder()
                .userId(userId)
                .uploadSessionId(sessionId)
                .mimeType("audio/mpeg")
                .contentHash("hash123")
                .build();

        // Act & Assert
        StepVerifier.create(handler.handle(command)).expectNextCount(1).verifyComplete();

        // Verification
        verify(mediaStorage).completeMultipartUpload(any(), any(), any());
        verify(ingestionPipeline).execute(any());
        assertThat(eventBus.getPublishedEvents()).hasSize(2); // UploadCompletedEvent + MediaIngestedEvent
    }
}

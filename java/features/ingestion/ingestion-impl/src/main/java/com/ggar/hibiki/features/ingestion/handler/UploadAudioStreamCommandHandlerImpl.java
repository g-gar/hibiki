package com.ggar.hibiki.features.ingestion.handler;

import com.ggar.hibiki.features.ingestion.dto.UploadAudioStreamCommand;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.generator.UuidV7IdGenerator;
import com.ggar.hibiki.features.ingestion.infrastructure.s3.MinioClientWrapper;
import com.ggar.hibiki.features.ingestion.infrastructure.tika.MediaTypeDetector;
import com.ggar.hibiki.features.ingestion.model.Media;
import com.ggar.hibiki.features.ingestion.model.User;
import com.ggar.hibiki.features.ingestion.port.MediaRepository;
import com.ggar.hibiki.features.ingestion.service.UploadAudioStreamCommandHandler;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class UploadAudioStreamCommandHandlerImpl implements UploadAudioStreamCommandHandler {
    private final MinioClientWrapper minioClientWrapper;
    private final MediaRepository mediaRepository;
    private final UuidV7IdGenerator idGenerator;
    private final MediaTypeDetector mediaTypeDetector;

    public UploadAudioStreamCommandHandlerImpl(MinioClientWrapper minioClientWrapper, MediaRepository mediaRepository) {
        this.minioClientWrapper = minioClientWrapper;
        this.mediaRepository = mediaRepository;
        this.idGenerator = new UuidV7IdGenerator();
        this.mediaTypeDetector = new MediaTypeDetector();
    }

    @Override
    public Mono<String> handle(UploadAudioStreamCommand command) {
        InputStream bufferedStream = new BufferedInputStream(command.getContentStream());

        return Mono.fromCallable(() -> {
                    UUID mediaId = idGenerator.generateId();
                    log.info("Starting ingestion, generated Media ID: {}", mediaId);

                    bufferedStream.mark(64 * 1024);
                    String detectedMimeType = mediaTypeDetector.detect(bufferedStream, mediaId.toString());
                    bufferedStream.reset();

                    log.info("Detected MIME type for {}: {}", mediaId, detectedMimeType);

                    return new Object[] {mediaId, detectedMimeType};
                })
                .flatMap(tuple -> {
                    UUID mediaId = (UUID) tuple[0];
                    String mimeType = (String) tuple[1];
                    long size = -1;

                    return minioClientWrapper
                            .uploadStream(mediaId.toString(), bufferedStream, size, mimeType)
                            .doOnSuccess(v -> log.info("Successfully uploaded stream for Media ID: {}", mediaId))
                            .thenReturn(tuple);
                })
                .flatMap(tuple -> {
                    UUID mediaId = (UUID) tuple[0];
                    String mimeType = (String) tuple[1];

                    Media media = Media.builder()
                            .id(mediaId)
                            .filename(mediaId.toString())
                            .mimeType(mimeType)
                            .size(0L)
                            .status("UPLOADED")
                            .uploadedAt(Instant.now())
                            .uploadedBy(
                                    command.getUserId() != null
                                            ? User.builder()
                                                    .id(UUID.fromString(command.getUserId()))
                                                    .build()
                                            : null)
                            .build();

                    return mediaRepository
                            .save(media)
                            .doOnSuccess(saved -> log.info("Saved Media for ID: {}", saved.getId()))
                            .thenReturn(mediaId.toString());
                })
                .doOnError(e -> log.error("Upload stream command handler failed.", e));
    }
}

package com.ggar.hibiki.features.ingestion.application.usecase;

import com.ggar.hibiki.features.ingestion.domain.AudioIngestionContent;
import com.ggar.hibiki.features.ingestion.domain.Media;
import com.ggar.hibiki.features.ingestion.domain.User;
import com.ggar.hibiki.features.ingestion.domain.port.MediaRepository;
import com.ggar.hibiki.features.ingestion.infrastructure.persistence.generator.UuidV7IdGenerator;
import com.ggar.hibiki.features.ingestion.infrastructure.s3.MinioClientWrapper;
import com.ggar.hibiki.features.ingestion.infrastructure.tika.MediaTypeDetector;
import com.ggar.hibiki.features.ingestion.logging.Logger;
import com.ggar.hibiki.features.ingestion.usecase.command.UploadAudioStreamCommand;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link UploadAudioStreamCommand}.
 * Orchestrates the uploading of audio streams, metadata extraction (via Tika),
 * MinIO persistence, and Neo4j database synchronization.
 */
@Service
public class UploadAudioStreamUseCase implements UploadAudioStreamCommand {

    private final Logger logger;
    private final MinioClientWrapper minioClientWrapper;
    private final MediaRepository mediaRepository;
    private final UuidV7IdGenerator idGenerator;
    private final MediaTypeDetector mediaTypeDetector;

    public UploadAudioStreamUseCase(
            Logger logger, MinioClientWrapper minioClientWrapper, MediaRepository mediaRepository) {
        this.logger = logger;
        this.minioClientWrapper = minioClientWrapper;
        this.mediaRepository = mediaRepository;
        this.idGenerator = new UuidV7IdGenerator();
        this.mediaTypeDetector = new MediaTypeDetector();
    }

    @Override
    public Mono<String> execute(AudioIngestionContent content) {
        InputStream bufferedStream = new BufferedInputStream(content.getContentStream());

        return Mono.fromCallable(() -> {
                    UUID mediaId = idGenerator.generateId();
                    logger.info("Starting ingestion, generated Media ID: {}", mediaId);

                    bufferedStream.mark(64 * 1024);
                    String detectedMimeType = mediaTypeDetector.detect(bufferedStream, mediaId.toString());
                    bufferedStream.reset();

                    logger.info("Detected MIME type for {}: {}", mediaId, detectedMimeType);

                    return new Object[] {mediaId, detectedMimeType};
                })
                .flatMap(tuple -> {
                    UUID mediaId = (UUID) tuple[0];
                    String mimeType = (String) tuple[1];
                    long size = -1;

                    return minioClientWrapper
                            .uploadStream(mediaId.toString(), bufferedStream, size, mimeType)
                            .doOnSuccess(v -> logger.info("Successfully uploaded stream for Media ID: {}", mediaId))
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
                                    content.getUserId() != null
                                            ? User.builder()
                                                    .id(UUID.fromString(content.getUserId()))
                                                    .build()
                                            : null)
                            .build();

                    return mediaRepository
                            .save(media)
                            .doOnSuccess(saved -> logger.info("Saved Media for ID: {}", saved.getId()))
                            .thenReturn(mediaId.toString());
                })
                .doOnError(e -> logger.error("Upload stream use case failed.", e));
    }
}

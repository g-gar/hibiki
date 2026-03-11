package com.ggar.hibiki.presentation.restapi.controller;

import com.ggar.hibiki.core.orchestrator.usecase.IngestMediaUseCase;
import com.ggar.hibiki.features.ingestion.model.IdentityContext;
import com.ggar.hibiki.features.ingestion.model.User;
import com.ggar.hibiki.features.ingestion.model.UserId;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * REST Controller responsible for handling ingestion boundaries.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/ingestion")
@RequiredArgsConstructor
public class IngestionController {

    private final IngestMediaUseCase ingestAudioFileUseCase;

    /**
     * Uploads an audio stream.
     * Starts a session, streams the chunks to storage, and completes it.
     * All synchronously within WebFlux, returning 202 once the file is fully ingested.
     *
     * @param filePartMono The incoming multipart file.
     * @return Mono completing with the accepted response and the Media ID.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<ResponseEntity<String>> uploadAudio(@RequestPart("file") Mono<FilePart> filePartMono) {

        // TODO: In a real environment, extract User from Spring Security context
        IdentityContext identityContext = IdentityContext.builder()
                .user(new User(new UserId(UUID.fromString("00000000-0000-0000-0000-000000000000"))))
                .build();

        return filePartMono
                .flatMap(filePart -> ingestAudioFileUseCase
                        .execute(
                                identityContext,
                                filePart.filename(),
                                filePart.headers().getContentLength(),
                                filePart.content())
                        .map(sessionId -> ResponseEntity.accepted()
                                .body("Ingestion completed for file " + filePart.filename() + ". Session ID: "
                                        + sessionId)))
                .onErrorResume(e -> {
                    log.error("Failed to process file upload", e);
                    return Mono.just(ResponseEntity.internalServerError().body("Upload failed: " + e.getMessage()));
                });
    }
}

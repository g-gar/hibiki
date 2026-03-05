package com.ggar.hibiki.presentation.restapi.controller;

import com.ggar.hibiki.core.orchestrator.application.usecase.IngestAudioFileUseCase;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
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
@RestController
@RequestMapping("/api/v1/ingestion")
public class IngestionController {

    private final IngestAudioFileUseCase ingestAudioFileUseCase;

    public IngestionController(IngestAudioFileUseCase ingestAudioFileUseCase) {
        this.ingestAudioFileUseCase = ingestAudioFileUseCase;
    }

    /**
     * Uploads an audio stream.
     *
     * @param filePartMono The incoming multipart file.
     * @return Mono completing with the accepted response and the Media ID.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<ResponseEntity<String>> uploadAudio(@RequestPart("file") Mono<FilePart> filePartMono) {

        return filePartMono.flatMap(filePart -> {
            try {
                PipedOutputStream os = new PipedOutputStream();
                PipedInputStream is = new PipedInputStream(os);

                filePart.content()
                        .subscribe(
                                dataBuffer -> {
                                    try {
                                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                        dataBuffer.read(bytes);
                                        os.write(bytes);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }
                                },
                                error -> {
                                    try {
                                        os.close();
                                    } catch (Exception ignored) {
                                    }
                                },
                                () -> {
                                    try {
                                        os.close();
                                    } catch (Exception ignored) {
                                    }
                                });

                IngestAudioFileUseCase.IngestAudioCommand command = new IngestAudioFileUseCase.IngestAudioCommand(is);

                return ingestAudioFileUseCase.handle(command).map(id -> ResponseEntity.accepted()
                        .body("Ingestion started. Media ID: " + id));

            } catch (Exception e) {
                return Mono.error(new RuntimeException("Error preparing stream", e));
            }
        });
    }
}

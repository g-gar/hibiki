package com.ggar.hibiki.presentation.restapi.controller;

import com.ggar.hibiki.core.orchestrator.usecase.IngestMediaUseCase;
import com.ggar.hibiki.features.metadata.model.FingerprintId;
import com.ggar.hibiki.features.metadata.model.MediaId;
import java.util.UUID;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/api/v1/metadata/webhooks")
@RequiredArgsConstructor
public class FingerprintWebhookController {

    private final IngestMediaUseCase ingestMediaUseCase;

    @PostMapping("/fingerprint")
    public Mono<ResponseEntity<Void>> handleFingerprintResult(@RequestBody FingerprintCallbackRequest request) {
        log.info("Received fingerprint callback for mediaId: {}", request.getMediaId());

        return ingestMediaUseCase
                .processMetadata(MediaId.of(request.getMediaId()), FingerprintId.of(request.getFingerprint()))
                .then(Mono.just(ResponseEntity.accepted().<Void>build()))
                .onErrorResume(e -> {
                    log.error("Error processing fingerprint callback for mediaId: {}", request.getMediaId(), e);
                    return Mono.just(ResponseEntity.internalServerError().build());
                });
    }

    // TODO: Remove this class
    @Data
    public static class FingerprintCallbackRequest {
        private UUID mediaId;
        private String fingerprint;
        private String status; // e.g., "SUCCESS", "ERROR"
        private String errorMessage;
    }
}

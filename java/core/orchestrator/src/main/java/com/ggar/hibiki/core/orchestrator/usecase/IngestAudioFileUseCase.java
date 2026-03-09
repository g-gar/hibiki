package com.ggar.hibiki.core.orchestrator.usecase;

import com.ggar.hibiki.core.orchestrator.dto.IngestAudioRequestDTO;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Orchestrator usecase for audio file ingestion.
 *
 * <p>TODO: Rewrite to use the new chunked upload API (InitiateUploadCommand,
 * UploadChunkCommand, CompleteUploadCommand). The old single-stream upload
 * has been replaced with a multi-part chunked upload protocol.
 */
@Service
public class IngestAudioFileUseCase extends BaseOrchestratorUseCase {

    public IngestAudioFileUseCase(Mediator mediator) {
        super(mediator);
    }

    public Mono<String> execute(IngestAudioRequestDTO requestDTO) {
        return Mono.error(new UnsupportedOperationException("Legacy single-stream upload removed. "
                + "Use the chunked upload API "
                + "(InitiateUpload -> UploadChunk -> CompleteUpload)."));
    }
}

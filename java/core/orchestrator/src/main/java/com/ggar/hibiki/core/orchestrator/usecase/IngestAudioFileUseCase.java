package com.ggar.hibiki.core.orchestrator.usecase;

import com.ggar.hibiki.core.orchestrator.dto.IngestAudioRequestDTO;
import com.ggar.hibiki.core.orchestrator.mapper.IngestAudioRequestMapper;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.features.ingestion.dto.UploadAudioStreamCommand;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Orchestrator usecase orchestrating the ingestion of an audio file.
 */
@Service
public class IngestAudioFileUseCase extends BaseOrchestratorUseCase {

    private final IngestAudioRequestMapper mapper;

    public IngestAudioFileUseCase(Mediator mediator, IngestAudioRequestMapper mapper) {
        super(mediator);
        this.mapper = mapper;
    }

    public Mono<String> execute(IngestAudioRequestDTO requestDTO) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(auth -> (String) auth.getPrincipal())
                .switchIfEmpty(Mono.error(new RuntimeException("No user authenticated")))
                .flatMap(userId -> {
                    UploadAudioStreamCommand uploadCommand = mapper.toCommand(requestDTO, userId);

                    return mediator.send(uploadCommand).doOnSuccess(mediaId -> {
                        // TODO: Publish AudioFileIngestedEvent to EventBus
                        // eventBus.publish(new AudioFileIngestedEvent(mediaId));
                    });
                });
    }
}

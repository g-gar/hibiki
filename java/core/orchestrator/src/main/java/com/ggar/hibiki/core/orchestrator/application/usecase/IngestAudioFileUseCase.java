package com.ggar.hibiki.core.orchestrator.application.usecase;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.ingestion.application.usecase.UploadAudioStreamUseCase;
import com.ggar.hibiki.features.ingestion.domain.AudioIngestionContent;
import java.io.InputStream;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Mediator command handler orchestrating the insertion of an audio file.
 */
@Service
public class IngestAudioFileUseCase implements CommandHandler<IngestAudioFileUseCase.IngestAudioCommand, Mono<String>> {

    private final UploadAudioStreamUseCase uploadAudioStreamUseCase;

    public IngestAudioFileUseCase(UploadAudioStreamUseCase uploadAudioStreamUseCase) {
        this.uploadAudioStreamUseCase = uploadAudioStreamUseCase;
    }

    @Override
    public Mono<String> handle(IngestAudioCommand command) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .map(auth -> (String) auth.getPrincipal())
                .switchIfEmpty(Mono.error(new RuntimeException("No user authenticated")))
                .flatMap(userId -> {
                    AudioIngestionContent content = new AudioIngestionContent(userId, command.inputStream());

                    return uploadAudioStreamUseCase.execute(content).doOnSuccess(mediaId -> {
                        // TODO: Publish AudioFileIngestedEvent to EventBus
                        // eventBus.publish(new AudioFileIngestedEvent(mediaId));
                    });
                });
    }

    public record IngestAudioCommand(InputStream inputStream) implements Command<Mono<String>> {}
}

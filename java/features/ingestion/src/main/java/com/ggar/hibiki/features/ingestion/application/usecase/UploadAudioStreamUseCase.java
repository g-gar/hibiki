package com.ggar.hibiki.features.ingestion.application.usecase;

import com.ggar.hibiki.features.ingestion.domain.AudioIngestionContent;
import reactor.core.publisher.Mono;

public interface UploadAudioStreamUseCase {

    /**
     * Processes an incoming audio stream, uploading it to storage and saving the metadata
     *
     * @param content the AudioIngestionContent
     * @return Mono completing with the generated ID
     */
    Mono<String> execute(AudioIngestionContent content);
}

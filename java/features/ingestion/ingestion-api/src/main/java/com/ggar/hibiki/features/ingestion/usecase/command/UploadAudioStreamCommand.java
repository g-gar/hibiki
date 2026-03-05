package com.ggar.hibiki.features.ingestion.usecase.command;

import com.ggar.hibiki.features.ingestion.domain.AudioIngestionContent;
import reactor.core.publisher.Mono;

public interface UploadAudioStreamCommand {

    /**
     * Processes an incoming audio stream, uploading it to storage and saving the
     * metadata
     *
     * @param content the AudioIngestionContent
     * @return Mono completing with the generated ID
     */
    Mono<String> execute(AudioIngestionContent content);
}

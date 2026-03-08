package com.ggar.hibiki.features.ingestion.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import java.io.InputStream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command representing the intent to upload and ingest an audio stream.
 *
 * <p>This command carries the user identifier and the raw {@link InputStream} of the audio content
 * to be processed, analyzed, and stored by the ingestion module.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadAudioStreamCommand implements Command<String> {
    private String userId;
    private InputStream contentStream;
}

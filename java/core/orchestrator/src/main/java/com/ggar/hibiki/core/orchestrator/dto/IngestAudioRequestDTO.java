package com.ggar.hibiki.core.orchestrator.dto;

import java.io.InputStream;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IngestAudioRequestDTO {
    private InputStream inputStream;
}

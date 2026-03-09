package com.ggar.hibiki.features.ingestion.pipeline;

import com.ggar.hibiki.features.ingestion.model.IngestionPhase;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.With;

/**
 * Immutable context object that flows through the ingestion pipeline.
 * Stages can enrich it via the attributes map.
 */
@Value
@Builder(toBuilder = true)
@With
public class IngestionContext {
    UUID mediaId;
    String s3Key;
    String mimeType;
    UUID userId;
    IngestionPhase currentPhase;

    @Singular
    Map<String, Object> attributes;
}

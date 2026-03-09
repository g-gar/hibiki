package com.ggar.hibiki.features.ingestion.pipeline;

import com.ggar.hibiki.features.ingestion.model.IngestionPhase;
import com.ggar.hibiki.features.ingestion.model.MediaId;
import com.ggar.hibiki.features.ingestion.model.UserId;
import java.util.Map;
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
    MediaId mediaId;
    String s3Key;
    String mimeType;
    UserId userId;
    IngestionPhase currentPhase;

    @Singular
    Map<String, Object> attributes;
}

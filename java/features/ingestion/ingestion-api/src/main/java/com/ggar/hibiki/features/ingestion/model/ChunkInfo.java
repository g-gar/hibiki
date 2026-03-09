package com.ggar.hibiki.features.ingestion.model;

import lombok.Builder;
import lombok.Value;

/**
 * Value object with chunk configuration for an upload item.
 */
@Value
@Builder
public class ChunkInfo {
    long chunkSize;
    int totalChunks;
}

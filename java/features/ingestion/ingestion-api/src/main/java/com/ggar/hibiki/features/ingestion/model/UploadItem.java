package com.ggar.hibiki.features.ingestion.model;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.With;

/**
 * Individual item within an upload session. Tracks chunk progress,
 * MIME type detection, and progressive hash accumulation for deduplication support.
 */
@Value
@Builder(toBuilder = true)
@With
public class UploadItem {
    UUID id;
    String originalFilename;
    String mimeType;
    long expectedSize;
    int receivedChunks;
    int totalChunks;
    IngestionPhase phase;
    UUID mediaId;
    String accumulatedHash;
    String error;
}

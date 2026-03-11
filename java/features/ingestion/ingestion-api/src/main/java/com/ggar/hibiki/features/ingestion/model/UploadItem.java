package com.ggar.hibiki.features.ingestion.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.With;
import lombok.experimental.FieldDefaults;

/**
 * Individual item within an upload session. Tracks chunk progress,
 * MIME type detection, and progressive hash accumulation for deduplication support.
 */
@Value
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder(toBuilder = true)
@With
public class UploadItem {
    UploadItemId id;
    String originalFilename;
    String mimeType;
    long expectedSize;
    int receivedChunks;
    int totalChunks;
    IngestionPhase phase;
    MediaId mediaId;
    String accumulatedHash;
    String error;
}

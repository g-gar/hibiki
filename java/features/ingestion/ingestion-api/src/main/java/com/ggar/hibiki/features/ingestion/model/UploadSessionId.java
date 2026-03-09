package com.ggar.hibiki.features.ingestion.model;

import java.util.UUID;
import lombok.Value;

/**
 * Identity reference for an upload session. Lightweight wrapper for cross-aggregate references.
 */
@Value
public class UploadSessionId {
    UUID id;
}

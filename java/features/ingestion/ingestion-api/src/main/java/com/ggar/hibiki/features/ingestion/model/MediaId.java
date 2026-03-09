package com.ggar.hibiki.features.ingestion.model;

import java.util.UUID;
import lombok.Value;

/**
 * Identity reference for a media item. Lightweight wrapper for cross-aggregate references.
 */
@Value
public class MediaId {
    UUID id;
}

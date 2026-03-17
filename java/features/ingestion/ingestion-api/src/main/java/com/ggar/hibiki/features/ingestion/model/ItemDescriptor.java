package com.ggar.hibiki.features.ingestion.model;

import lombok.Builder;
import lombok.Value;

/**
 * Declarative descriptor for an item to be uploaded.
 */
@Value
@Builder
public class ItemDescriptor {
    String originalFilename;
    long expectedSize;
}

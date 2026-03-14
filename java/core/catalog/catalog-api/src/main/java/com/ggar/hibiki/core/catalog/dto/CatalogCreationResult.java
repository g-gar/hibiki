package com.ggar.hibiki.core.catalog.dto;

import lombok.Builder;
import lombok.Value;
import java.util.UUID;

/**
 * Result of creating items in the catalog.
 */
@Value
@Builder
public class CatalogCreationResult {
    UUID songId;
    UUID albumId;
}

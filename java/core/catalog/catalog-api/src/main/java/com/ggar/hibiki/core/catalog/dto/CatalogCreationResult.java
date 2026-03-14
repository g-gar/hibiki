package com.ggar.hibiki.core.catalog.dto;

import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Result of creating items in the catalog.
 */
@Value
@Builder
public class CatalogCreationResult {
    UUID songId;
    UUID albumId;
}

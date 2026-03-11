package com.ggar.hibiki.core.catalog.dto;

import lombok.Builder;
import lombok.Value;

/**
 * Result of creating items in the catalog.
 */
@Value
@Builder
public class CatalogCreationResult {
    String songId;
    String albumId;
}

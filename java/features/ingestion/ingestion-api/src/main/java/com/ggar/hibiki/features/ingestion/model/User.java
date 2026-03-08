package com.ggar.hibiki.features.ingestion.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a user within the ingestion domain context.
 *
 * <p>Typically used to associate ingested media with the user who initiated the upload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
}

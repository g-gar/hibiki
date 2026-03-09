package com.ggar.hibiki.features.ingestion.model;

import lombok.Value;

/**
 * Immutable user identity for ingestion operations.
 */
@Value
public class User {
    UserId id;
}

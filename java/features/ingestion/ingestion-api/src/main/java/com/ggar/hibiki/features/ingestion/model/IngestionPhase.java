package com.ggar.hibiki.features.ingestion.model;

/**
 * Represents the phases of the ingestion process lifecycle.
 */
public enum IngestionPhase {
    INITIATED,
    UPLOADING,
    UPLOADED,
    PROCESSING,
    COMPLETED,
    FAILED,
    CANCELLED
}

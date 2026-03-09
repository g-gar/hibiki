package com.ggar.hibiki.features.ingestion.model;

/**
 * Status of a persisted media entity, replacing the previous free-form String status.
 */
public enum MediaStatus {
    PENDING,
    READY,
    PROCESSING,
    FAILED
}

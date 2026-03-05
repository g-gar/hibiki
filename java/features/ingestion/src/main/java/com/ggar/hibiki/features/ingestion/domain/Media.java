package com.ggar.hibiki.features.ingestion.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Media {
    private UUID id;
    private String filename;
    private String mimeType;
    private Long size;
    private String status;
    private Instant uploadedAt;
    private User uploadedBy;
}

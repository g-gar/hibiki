package com.ggar.hibiki.features.ingestion.dto;

import com.ggar.hibiki.features.ingestion.model.IngestionPhase;
import java.time.Instant;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UploadSessionDto {
    String id;
    String userId;
    List<UploadItemDto> items;
    IngestionPhase phase;
    Instant createdAt;
    Instant completedAt;

    @Value
    @Builder
    public static class UploadItemDto {
        String id;
        String originalFilename;
        long expectedSize;
        IngestionPhase phase;
    }
}

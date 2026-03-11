package com.ggar.hibiki.features.ingestion.dto;

import com.ggar.hibiki.features.ingestion.model.IngestionPhase;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UploadProgressDto {
    String uploadSessionId;
    String itemId;
    IngestionPhase phase;
    int progress;
    String mediaId;
    String error;
}

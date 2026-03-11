package com.ggar.hibiki.features.ingestion.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;

/**
 * Initiates a new upload session with one or more items.
 */
@Value
@Builder
public class InitiateUploadCommand implements Command<UploadSessionDto> {
    UUID userId;
    List<ItemDescriptor> items;
}

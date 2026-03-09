package com.ggar.hibiki.features.ingestion.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.features.ingestion.model.IdentityContext;
import com.ggar.hibiki.features.ingestion.model.UploadSession;
import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Initiates a new upload session with one or more items.
 */
@Value
@Builder
public class InitiateUploadCommand implements Command<UploadSession> {
    IdentityContext identityContext;
    List<ItemDescriptor> items;
}

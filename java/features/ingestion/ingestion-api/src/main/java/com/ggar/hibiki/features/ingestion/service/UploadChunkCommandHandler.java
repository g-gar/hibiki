package com.ggar.hibiki.features.ingestion.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.ingestion.dto.UploadChunkCommand;
import com.ggar.hibiki.features.ingestion.dto.UploadProgressDto;

public interface UploadChunkCommandHandler extends CommandHandler<UploadChunkCommand, UploadProgressDto> {}

package com.ggar.hibiki.features.ingestion.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.ingestion.dto.UploadChunkCommand;
import com.ggar.hibiki.features.ingestion.model.UploadProgress;

public interface UploadChunkCommandHandler extends CommandHandler<UploadChunkCommand, UploadProgress> {}

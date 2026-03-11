package com.ggar.hibiki.features.ingestion.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.ingestion.dto.CompleteUploadCommand;
import com.ggar.hibiki.features.ingestion.dto.UploadSessionDto;

public interface CompleteUploadCommandHandler extends CommandHandler<CompleteUploadCommand, UploadSessionDto> {}

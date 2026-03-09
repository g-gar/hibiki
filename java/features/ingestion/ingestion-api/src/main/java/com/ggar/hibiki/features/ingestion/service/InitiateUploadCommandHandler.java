package com.ggar.hibiki.features.ingestion.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.ingestion.dto.InitiateUploadCommand;
import com.ggar.hibiki.features.ingestion.model.UploadSession;

public interface InitiateUploadCommandHandler extends CommandHandler<InitiateUploadCommand, UploadSession> {}

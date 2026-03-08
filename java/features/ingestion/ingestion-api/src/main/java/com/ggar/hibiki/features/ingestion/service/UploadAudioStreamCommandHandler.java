package com.ggar.hibiki.features.ingestion.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.ingestion.dto.UploadAudioStreamCommand;

/**
 * Handler interface for the {@link UploadAudioStreamCommand}.
 *
 * <p>Implementations of this interface are responsible for executing the workflow associated
 * with ingesting a new audio stream, such as storage allocation, metadata extraction, and persistence.
 */
public interface UploadAudioStreamCommandHandler extends CommandHandler<UploadAudioStreamCommand, String> {}

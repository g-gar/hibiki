package com.ggar.hibiki.features.metadata.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.metadata.dto.FetchMetadataCommand;
import com.ggar.hibiki.features.metadata.model.FetchMetadataResult;

/**
 * Contract for a handler that fetches metadata from external sources using a fingerprint.
 */
public interface FetchMetadataCommandHandler extends CommandHandler<FetchMetadataCommand, FetchMetadataResult> {}

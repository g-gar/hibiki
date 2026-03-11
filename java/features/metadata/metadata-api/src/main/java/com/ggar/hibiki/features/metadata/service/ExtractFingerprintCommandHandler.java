package com.ggar.hibiki.features.metadata.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.metadata.dto.ExtractFingerprintCommand;
import com.ggar.hibiki.features.metadata.model.FingerprintResult;

/**
 * Contract for a handler that extracts a fingerprint from a media source.
 */
public interface ExtractFingerprintCommandHandler
        extends CommandHandler<ExtractFingerprintCommand, FingerprintResult> {}

package com.ggar.hibiki.features.metadata.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.metadata.dto.MapToId3Command;
import com.ggar.hibiki.features.metadata.model.Id3Result;

/**
 * Contract for a handler that maps raw external metadata into a standard ID3v2 tag format.
 */
public interface MapToId3CommandHandler extends CommandHandler<MapToId3Command, Id3Result> {}

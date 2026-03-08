package com.ggar.hibiki.features.history.service;

import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.history.dto.RecordPlaybackCommand;
import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;

/**
 * Inbound service for handling playback recording commands.
 */
public interface RecordPlaybackCommandHandler extends CommandHandler<RecordPlaybackCommand, PlaybackHistoryEntry> {}

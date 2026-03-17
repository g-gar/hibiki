package com.ggar.hibiki.features.history.handler.command;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import com.ggar.hibiki.features.history.model.PlaybackHistoryEntry;
import java.util.UUID;

/**
 * Interface for the handler responsible for recording playback events.
 */
public interface RecordPlaybackCommandHandler
        extends CommandHandler<RecordPlaybackCommandHandler.Record, PlaybackHistoryEntry> {

    /**
     * Data needed to record a playback event.
     */
    record Record(UUID userId, UUID sessionId, UUID deviceId, UUID songId, String contextType, UUID contextId)
            implements Command<PlaybackHistoryEntry> {}

    /**
     * Event published when a playback is successfully recorded.
     */
    record Recorded(UUID playbackId, UUID userId, UUID songId) implements DomainEvent {}
}

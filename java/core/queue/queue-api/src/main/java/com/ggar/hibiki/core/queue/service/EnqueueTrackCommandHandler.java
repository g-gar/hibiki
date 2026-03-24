package com.ggar.hibiki.core.queue.service;

import com.ggar.hibiki.core.queue.model.PlaybackQueue;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.List;
import java.util.UUID;

/** Adds one or more tracks to the end of the queue (or at a specific position). */
public interface EnqueueTrackCommandHandler extends CommandHandler<EnqueueTrackCommandHandler.Enqueue, PlaybackQueue> {

    record Enqueue(
            UUID sessionId,
            List<UUID> trackIds,
            /** Si null, añade al final. Si se especifica, inserta en esa posición. */
            Integer atIndex)
            implements Command<PlaybackQueue> {}
}

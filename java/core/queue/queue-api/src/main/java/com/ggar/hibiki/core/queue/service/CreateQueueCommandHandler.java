package com.ggar.hibiki.core.queue.service;

import com.ggar.hibiki.core.queue.model.PlaybackQueue;
import com.ggar.hibiki.core.queue.model.RepeatMode;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.List;
import java.util.UUID;

/** Creates a new queue associated with a session, optionally with initial tracks. */
public interface CreateQueueCommandHandler extends CommandHandler<CreateQueueCommandHandler.Create, PlaybackQueue> {

    record Create(
            UUID sessionId,
            List<UUID> initialTracks, // puede estar vacía
            RepeatMode repeatMode,
            boolean shuffle)
            implements Command<PlaybackQueue> {}
}

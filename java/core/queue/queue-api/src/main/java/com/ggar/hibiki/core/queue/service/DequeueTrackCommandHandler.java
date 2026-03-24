package com.ggar.hibiki.core.queue.service;

import com.ggar.hibiki.core.queue.model.PlaybackQueue;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/** Removes a specific track from the queue by its position. */
public interface DequeueTrackCommandHandler extends CommandHandler<DequeueTrackCommandHandler.Dequeue, PlaybackQueue> {

    record Dequeue(UUID sessionId, int index) implements Command<PlaybackQueue> {}
}

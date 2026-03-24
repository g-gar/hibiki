package com.ggar.hibiki.core.queue.service;

import com.ggar.hibiki.core.queue.model.TrackId;
import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * Advances to the next track according to the active playback policy.
 * Returns the TrackId of the new active track (or empty if the queue is exhausted).
 */
public interface NextTrackCommandHandler extends CommandHandler<NextTrackCommandHandler.Next, Optional<TrackId>> {

    record Next(UUID sessionId) implements Command<Optional<TrackId>> {}

    /** Published when the queue has no more tracks and repeatMode = OFF. */
    record Exhausted(UUID sessionId, Instant timestamp) implements DomainEvent {}
}

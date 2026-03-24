package com.ggar.hibiki.core.queue.service;

import com.ggar.hibiki.core.queue.model.TrackId;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.Optional;
import java.util.UUID;

/** Goes back to the previous track. If at the first one, does nothing. */
public interface PreviousTrackCommandHandler
        extends CommandHandler<PreviousTrackCommandHandler.Previous, Optional<TrackId>> {

    record Previous(UUID sessionId) implements Command<Optional<TrackId>> {}
}

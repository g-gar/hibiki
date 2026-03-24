package com.ggar.hibiki.core.queue.service;

import com.ggar.hibiki.core.queue.model.PlaybackQueue;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/** Enables or disables shuffle mode. Reorders the list internally. */
public interface SetShuffleCommandHandler extends CommandHandler<SetShuffleCommandHandler.SetShuffle, PlaybackQueue> {

    record SetShuffle(UUID sessionId, boolean enabled) implements Command<PlaybackQueue> {}
}

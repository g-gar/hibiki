package com.ggar.hibiki.core.queue.service;

import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/** Clears the queue completely (except for the currently active track). */
public interface ClearQueueCommandHandler extends CommandHandler<ClearQueueCommandHandler.Clear, Void> {

    record Clear(UUID sessionId) implements Command<Void> {}
}

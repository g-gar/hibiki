package com.ggar.hibiki.core.queue.service;

import com.ggar.hibiki.core.queue.model.PlaybackQueue;
import com.ggar.hibiki.core.queue.model.RepeatMode;
import com.ggar.hibiki.core.shared.mediator.Command;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import java.util.UUID;

/** Updates the repeat policy of the active queue. */
public interface UpdateRepeatModeCommandHandler
        extends CommandHandler<UpdateRepeatModeCommandHandler.Update, PlaybackQueue> {

    record Update(UUID sessionId, RepeatMode repeatMode) implements Command<PlaybackQueue> {}
}

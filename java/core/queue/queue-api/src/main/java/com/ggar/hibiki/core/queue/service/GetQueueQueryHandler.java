package com.ggar.hibiki.core.queue.service;

import com.ggar.hibiki.core.queue.model.PlaybackQueue;
import com.ggar.hibiki.core.shared.mediator.Query;
import com.ggar.hibiki.core.shared.mediator.QueryHandler;
import java.util.UUID;

/** Returns the full state of the queue for a given session. */
public interface GetQueueQueryHandler extends QueryHandler<GetQueueQueryHandler.Get, PlaybackQueue> {

    record Get(UUID sessionId) implements Query<PlaybackQueue> {}
}

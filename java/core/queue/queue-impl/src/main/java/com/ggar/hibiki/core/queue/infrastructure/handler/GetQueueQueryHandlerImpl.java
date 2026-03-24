package com.ggar.hibiki.core.queue.infrastructure.handler;

import com.ggar.hibiki.core.queue.model.PlaybackQueue;
import com.ggar.hibiki.core.queue.model.SessionId;
import com.ggar.hibiki.core.queue.port.PlaybackQueueRepository;
import com.ggar.hibiki.core.queue.service.GetQueueQueryHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GetQueueQueryHandlerImpl implements GetQueueQueryHandler {

    private final PlaybackQueueRepository repository;

    @Override
    public Mono<PlaybackQueue> handle(Get command) {
        return repository.findBySessionId(SessionId.of(command.sessionId()));
    }
}

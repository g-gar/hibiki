package com.ggar.hibiki.core.queue.infrastructure.handler;

import com.ggar.hibiki.core.queue.model.PlaybackQueue;
import com.ggar.hibiki.core.queue.model.SessionId;
import com.ggar.hibiki.core.queue.port.PlaybackQueueRepository;
import com.ggar.hibiki.core.queue.service.UpdateRepeatModeCommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateRepeatModeCommandHandlerImpl implements UpdateRepeatModeCommandHandler {

    private final PlaybackQueueRepository repository;

    @Override
    public Mono<PlaybackQueue> handle(Update command) {
        return repository
                .findBySessionId(SessionId.of(command.sessionId()))
                .flatMap(queue -> repository.save(
                        queue.toBuilder().repeatMode(command.repeatMode()).build()));
    }
}

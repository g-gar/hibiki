package com.ggar.hibiki.core.queue.infrastructure.handler;

import com.ggar.hibiki.core.queue.model.PlaybackQueue;
import com.ggar.hibiki.core.queue.model.QueueId;
import com.ggar.hibiki.core.queue.model.RepeatMode;
import com.ggar.hibiki.core.queue.model.SessionId;
import com.ggar.hibiki.core.queue.model.TrackId;
import com.ggar.hibiki.core.queue.port.PlaybackQueueRepository;
import com.ggar.hibiki.core.queue.service.CreateQueueCommandHandler;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateQueueCommandHandlerImpl implements CreateQueueCommandHandler {

    private final PlaybackQueueRepository repository;

    @Override
    public Mono<PlaybackQueue> handle(Create command) {
        PlaybackQueue queue = PlaybackQueue.builder()
                .queueId(QueueId.next())
                .sessionId(SessionId.of(command.sessionId()))
                .tracks(command.initialTracks().stream().map(TrackId::of).collect(Collectors.toList()))
                .currentIndex(0)
                .repeatMode(command.repeatMode() != null ? command.repeatMode() : RepeatMode.OFF)
                .shuffled(command.shuffle())
                .build();

        // Note: If shuffle is true, we might want to call ShuffleEngine here,
        // but for now we follow the simple creation.

        return repository.save(queue);
    }
}

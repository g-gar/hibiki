package com.ggar.hibiki.core.queue.infrastructure.handler;

import com.ggar.hibiki.core.queue.model.PlaybackQueue;
import com.ggar.hibiki.core.queue.model.SessionId;
import com.ggar.hibiki.core.queue.model.TrackId;
import com.ggar.hibiki.core.queue.port.PlaybackQueueRepository;
import com.ggar.hibiki.core.queue.service.EnqueueTrackCommandHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class EnqueueTrackCommandHandlerImpl implements EnqueueTrackCommandHandler {

    private final PlaybackQueueRepository repository;

    @Override
    public Mono<PlaybackQueue> handle(Enqueue command) {
        return repository
                .findBySessionId(SessionId.of(command.sessionId()))
                .map(queue -> {
                    List<TrackId> currentTracks = new ArrayList<>(queue.getTracks());
                    List<TrackId> newTracks =
                            command.trackIds().stream().map(TrackId::of).collect(Collectors.toList());

                    if (command.atIndex() == null || command.atIndex() >= currentTracks.size()) {
                        currentTracks.addAll(newTracks);
                    } else {
                        currentTracks.addAll(Math.max(0, command.atIndex()), newTracks);
                    }

                    return queue.toBuilder().tracks(currentTracks).build();
                })
                .flatMap(repository::save);
    }
}

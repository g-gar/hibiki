package com.ggar.hibiki.core.queue.infrastructure.handler;

import com.ggar.hibiki.core.queue.model.SessionId;
import com.ggar.hibiki.core.queue.model.TrackId;
import com.ggar.hibiki.core.queue.port.PlaybackQueueRepository;
import com.ggar.hibiki.core.queue.service.ClearQueueCommandHandler;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ClearQueueCommandHandlerImpl implements ClearQueueCommandHandler {

    private final PlaybackQueueRepository repository;

    @Override
    public Mono<Void> handle(Clear command) {
        return repository
                .findBySessionId(SessionId.of(command.sessionId()))
                .flatMap(queue -> {
                    // We keep only the currently active track if it exists
                    List<TrackId> tracks = queue.getTracks();
                    List<TrackId> newTracks = Collections.emptyList();
                    int newIndex = 0;

                    if (!tracks.isEmpty() && queue.getCurrentIndex() >= 0 && queue.getCurrentIndex() < tracks.size()) {
                        newTracks = List.of(tracks.get(queue.getCurrentIndex()));
                    }

                    return repository.save(queue.toBuilder()
                            .tracks(newTracks)
                            .currentIndex(newIndex)
                            .build());
                })
                .then();
    }
}

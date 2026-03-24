package com.ggar.hibiki.core.queue.infrastructure.handler;

import com.ggar.hibiki.core.queue.model.SessionId;
import com.ggar.hibiki.core.queue.model.TrackId;
import com.ggar.hibiki.core.queue.port.PlaybackQueueRepository;
import com.ggar.hibiki.core.queue.service.NextTrackCommandHandler;
import com.ggar.hibiki.core.shared.event.EventBus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class NextTrackCommandHandlerImpl implements NextTrackCommandHandler {

    private final PlaybackQueueRepository repository;
    private final EventBus eventBus;

    @Override
    public Mono<Optional<TrackId>> handle(Next command) {
        return repository.findBySessionId(SessionId.of(command.sessionId())).flatMap(queue -> {
            List<TrackId> tracks = queue.getTracks();
            int nextIndex = queue.getCurrentIndex() + 1;

            if (nextIndex < tracks.size()) {
                return repository
                        .save(queue.toBuilder().currentIndex(nextIndex).build())
                        .map(q -> Optional.of(tracks.get(nextIndex)));
            }

            // Queue exhausted, check repeat mode
            switch (queue.getRepeatMode()) {
                case ONE:
                    return Mono.just(Optional.of(tracks.get(queue.getCurrentIndex())));
                case ALL:
                    if (tracks.isEmpty()) {
                        return Mono.just(Optional.empty());
                    }
                    return repository
                            .save(queue.toBuilder().currentIndex(0).build())
                            .map(q -> Optional.of(tracks.get(0)));
                default: // OFF
                    eventBus.publish(new Exhausted(command.sessionId(), Instant.now()));
                    return Mono.just(Optional.empty());
            }
        });
    }
}

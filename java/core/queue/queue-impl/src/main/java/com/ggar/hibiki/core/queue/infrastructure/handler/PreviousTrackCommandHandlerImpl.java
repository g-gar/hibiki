package com.ggar.hibiki.core.queue.infrastructure.handler;

import com.ggar.hibiki.core.queue.model.SessionId;
import com.ggar.hibiki.core.queue.model.TrackId;
import com.ggar.hibiki.core.queue.port.PlaybackQueueRepository;
import com.ggar.hibiki.core.queue.service.PreviousTrackCommandHandler;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PreviousTrackCommandHandlerImpl implements PreviousTrackCommandHandler {

    private final PlaybackQueueRepository repository;

    @Override
    public Mono<Optional<TrackId>> handle(Previous command) {
        return repository.findBySessionId(SessionId.of(command.sessionId())).flatMap(queue -> {
            int prevIndex = queue.getCurrentIndex() - 1;
            if (prevIndex < 0) {
                return Mono.just(Optional.ofNullable(
                        queue.getTracks().isEmpty() ? null : queue.getTracks().get(0)));
            }

            return repository
                    .save(queue.toBuilder().currentIndex(prevIndex).build())
                    .map(q -> Optional.of(q.getTracks().get(prevIndex)));
        });
    }
}

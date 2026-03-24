package com.ggar.hibiki.core.queue.infrastructure.handler;

import com.ggar.hibiki.core.queue.model.PlaybackQueue;
import com.ggar.hibiki.core.queue.model.SessionId;
import com.ggar.hibiki.core.queue.port.PlaybackQueueRepository;
import com.ggar.hibiki.core.queue.service.DequeueTrackCommandHandler;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DequeueTrackCommandHandlerImpl implements DequeueTrackCommandHandler {

    private final PlaybackQueueRepository repository;

    @Override
    public Mono<PlaybackQueue> handle(Dequeue command) {
        return repository.findBySessionId(SessionId.of(command.sessionId())).flatMap(queue -> {
            List<com.ggar.hibiki.core.queue.model.TrackId> tracks = new ArrayList<>(queue.getTracks());
            if (command.index() < 0 || command.index() >= tracks.size()) {
                return Mono.just(queue);
            }

            tracks.remove(command.index());

            int newCurrentIndex = queue.getCurrentIndex();
            if (command.index() < newCurrentIndex) {
                newCurrentIndex--;
            }

            return repository.save(queue.toBuilder()
                    .tracks(tracks)
                    .currentIndex(newCurrentIndex)
                    .build());
        });
    }
}

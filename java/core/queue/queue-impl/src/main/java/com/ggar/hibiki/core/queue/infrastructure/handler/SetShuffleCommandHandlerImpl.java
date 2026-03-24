package com.ggar.hibiki.core.queue.infrastructure.handler;

import com.ggar.hibiki.core.queue.infrastructure.shuffle.ShuffleEngine;
import com.ggar.hibiki.core.queue.model.PlaybackQueue;
import com.ggar.hibiki.core.queue.model.SessionId;
import com.ggar.hibiki.core.queue.model.TrackId;
import com.ggar.hibiki.core.queue.port.PlaybackQueueRepository;
import com.ggar.hibiki.core.queue.service.SetShuffleCommandHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SetShuffleCommandHandlerImpl implements SetShuffleCommandHandler {

    private final PlaybackQueueRepository repository;
    private final ShuffleEngine shuffleEngine;

    @Override
    public Mono<PlaybackQueue> handle(SetShuffle command) {
        return repository.findBySessionId(SessionId.of(command.sessionId())).flatMap(queue -> {
            if (queue.isShuffled() == command.enabled()) {
                return Mono.just(queue);
            }

            List<TrackId> newTracks = queue.getTracks();
            int newIndex = queue.getCurrentIndex();

            if (command.enabled()) {
                newTracks = shuffleEngine.shuffle(newTracks, newIndex);
                newIndex = 0; // The current track was moved to 0 by ShuffleEngine
            } else {
                // Turning off shuffle ideally restores original order,
                // but we don't store it. For now, we just stay with the current order.
                // In a real app, we might want to store the original order.
            }

            return repository.save(queue.toBuilder()
                    .tracks(newTracks)
                    .currentIndex(newIndex)
                    .shuffled(command.enabled())
                    .build());
        });
    }
}

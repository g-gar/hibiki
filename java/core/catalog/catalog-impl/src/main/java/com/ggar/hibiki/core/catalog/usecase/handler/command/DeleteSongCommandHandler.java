package com.ggar.hibiki.core.catalog.usecase.handler.command;

import com.ggar.hibiki.core.catalog.dto.DeleteSongCommand;
import com.ggar.hibiki.core.catalog.event.SongDeletedEvent;
import com.ggar.hibiki.core.catalog.port.SongRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteSongCommandHandler implements CommandHandler<DeleteSongCommand, Void> {

    private final SongRepository songRepository;
    private final EventBus eventBus;

    @Override
    public Mono<Void> handle(DeleteSongCommand command) {
        return songRepository
                .findById(command.getId())
                .flatMap(song -> songRepository
                        .deleteById(song.getId())
                        .then(eventBus.publish(SongDeletedEvent.builder()
                                .songId(song.getId())
                                .title(song.getTitle())
                                .build())))
                .then();
    }
}

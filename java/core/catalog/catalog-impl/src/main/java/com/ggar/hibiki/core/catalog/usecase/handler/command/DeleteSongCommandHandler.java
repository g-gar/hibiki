package com.ggar.hibiki.core.catalog.usecase.handler.command;

import com.ggar.hibiki.core.catalog.dto.DeleteSongCommand;
import com.ggar.hibiki.core.catalog.persistence.repository.SongRepository;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteSongCommandHandler implements CommandHandler<DeleteSongCommand, Void> {

    private final SongRepository songRepository;

    @Override
    public Mono<Void> handle(DeleteSongCommand command) {
        return songRepository.deleteById(command.getId());
    }
}

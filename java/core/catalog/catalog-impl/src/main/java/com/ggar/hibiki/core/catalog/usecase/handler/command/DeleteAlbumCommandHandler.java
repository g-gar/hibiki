package com.ggar.hibiki.core.catalog.usecase.handler.command;

import com.ggar.hibiki.core.catalog.dto.DeleteAlbumCommand;
import com.ggar.hibiki.core.catalog.port.AlbumRepository;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteAlbumCommandHandler implements CommandHandler<DeleteAlbumCommand, Void> {

    private final AlbumRepository albumRepository;

    @Override
    public Mono<Void> handle(DeleteAlbumCommand command) {
        return albumRepository.deleteById(command.getId());
    }
}

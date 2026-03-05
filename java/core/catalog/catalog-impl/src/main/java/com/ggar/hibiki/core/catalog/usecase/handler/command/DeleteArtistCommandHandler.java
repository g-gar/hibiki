package com.ggar.hibiki.core.catalog.usecase.handler.command;

import com.ggar.hibiki.core.catalog.model.command.DeleteArtistCommand;
import com.ggar.hibiki.core.catalog.persistence.repository.ArtistRepository;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteArtistCommandHandler implements CommandHandler<DeleteArtistCommand, Void> {

    private final ArtistRepository artistRepository;

    @Override
    public Mono<Void> handle(DeleteArtistCommand command) {
        return artistRepository.deleteById(command.getId());
    }
}

package com.ggar.hibiki.core.catalog.usecase.handler.command;

import com.ggar.hibiki.core.catalog.dto.DeleteArtistCommand;
import com.ggar.hibiki.core.catalog.event.ArtistDeletedEvent;
import com.ggar.hibiki.core.catalog.persistence.repository.AlbumRepository;
import com.ggar.hibiki.core.catalog.persistence.repository.ArtistRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteArtistCommandHandler implements CommandHandler<DeleteArtistCommand, Void> {

    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final EventBus eventBus;

    @Override
    public Mono<Void> handle(DeleteArtistCommand command) {
        return artistRepository.findById(command.getId()).flatMap(artist -> {
            // 1. Publish event
            ArtistDeletedEvent event = ArtistDeletedEvent.builder()
                    .artistId(artist.getId())
                    .name(artist.getName())
                    .build();

            return eventBus.publish(event).then(artistRepository.deleteById(artist.getId()));
        });
    }
}

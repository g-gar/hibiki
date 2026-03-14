package com.ggar.hibiki.core.catalog.usecase.handler.command;

import com.ggar.hibiki.core.catalog.dto.ArtistDto;
import com.ggar.hibiki.core.catalog.dto.CreateArtistCommand;
import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.catalog.persistence.mapper.ArtistMapper;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateArtistCommandHandler implements CommandHandler<CreateArtistCommand, ArtistDto> {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    @Override
    public Mono<ArtistDto> handle(CreateArtistCommand command) {
        return artistRepository
                .findByName(command.getName())
                .switchIfEmpty(Mono.defer(() -> {
                    Artist artist = Artist.builder().name(command.getName()).build();
                    return artistRepository.save(artist);
                }))
                .map(artistMapper::toDto);
    }
}

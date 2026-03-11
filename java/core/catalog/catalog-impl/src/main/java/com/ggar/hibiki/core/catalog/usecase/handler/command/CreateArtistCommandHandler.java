package com.ggar.hibiki.core.catalog.usecase.handler.command;

import com.ggar.hibiki.core.catalog.dto.ArtistDto;
import com.ggar.hibiki.core.catalog.dto.CreateArtistCommand;
import com.ggar.hibiki.core.catalog.persistence.entity.ArtistEntity;
import com.ggar.hibiki.core.catalog.persistence.mapper.ArtistMapper;
import com.ggar.hibiki.core.catalog.persistence.repository.ArtistRepository;
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
                .findByNameIgnoreCase(command.getName())
                .switchIfEmpty(Mono.defer(() -> {
                    ArtistEntity entity = new ArtistEntity(command.getName());
                    return artistRepository.save(entity);
                }))
                .map(artistMapper::toDto);
    }
}

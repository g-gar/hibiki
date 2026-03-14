package com.ggar.hibiki.core.catalog.usecase.handler.command;

import com.ggar.hibiki.core.catalog.dto.ArtistDto;
import com.ggar.hibiki.core.catalog.dto.UpdateArtistCommand;
import com.ggar.hibiki.core.catalog.persistence.mapper.ArtistMapper;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateArtistCommandHandler implements CommandHandler<UpdateArtistCommand, ArtistDto> {

    private final ArtistRepository artistRepository;
    private final ArtistMapper artistMapper;

    @Override
    public Mono<ArtistDto> handle(UpdateArtistCommand command) {
        return artistRepository
                .findById(command.getId())
                .flatMap(artist -> {
                    if (command.getName() != null) artist.setName(command.getName());
                    if (command.getIsni() != null) artist.setIsni(command.getIsni());
                    return artistRepository.save(artist);
                })
                .map(artistMapper::toDto);
    }
}

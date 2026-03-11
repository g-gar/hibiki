package com.ggar.hibiki.core.catalog.usecase.handler.command;

import com.ggar.hibiki.core.catalog.dto.AlbumDto;
import com.ggar.hibiki.core.catalog.dto.CreateAlbumCommand;
import com.ggar.hibiki.core.catalog.persistence.entity.AlbumEntity;
import com.ggar.hibiki.core.catalog.persistence.mapper.AlbumMapper;
import com.ggar.hibiki.core.catalog.persistence.repository.AlbumRepository;
import com.ggar.hibiki.core.catalog.persistence.repository.ArtistRepository;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateAlbumCommandHandler implements CommandHandler<CreateAlbumCommand, AlbumDto> {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final AlbumMapper albumMapper;

    @Override
    public Mono<AlbumDto> handle(CreateAlbumCommand command) {
        return albumRepository
                .findByTitleIgnoreCaseAndArtistNameIgnoreCase(command.getTitle(), command.getArtistName())
                .switchIfEmpty(Mono.defer(() -> artistRepository
                        .findByNameIgnoreCase(command.getArtistName())
                        .flatMap(artist -> {
                            AlbumEntity album = new AlbumEntity(command.getTitle(), command.getReleaseYear());
                            album.setArtist(artist);
                            return albumRepository.save(album);
                        })))
                .map(albumMapper::toDto);
    }
}

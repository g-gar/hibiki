package com.ggar.hibiki.core.catalog.usecase.handler.command;

import com.ggar.hibiki.core.catalog.dto.AlbumDto;
import com.ggar.hibiki.core.catalog.dto.CreateAlbumCommand;
import com.ggar.hibiki.core.catalog.model.Album;
import com.ggar.hibiki.core.catalog.persistence.mapper.AlbumMapper;
import com.ggar.hibiki.core.catalog.port.AlbumRepository;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
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
                .findByTitleAndArtist(command.getTitle(), command.getArtistName())
                .switchIfEmpty(Mono.defer(() -> artistRepository
                        .findByName(command.getArtistName())
                        .flatMap(artist -> {
                            Album album = Album.builder()
                                    .title(command.getTitle())
                                    .releaseYear(command.getReleaseYear())
                                    .artist(artist)
                                    .build();
                            return albumRepository.save(album);
                        })))
                .map(albumMapper::toDto);
    }
}

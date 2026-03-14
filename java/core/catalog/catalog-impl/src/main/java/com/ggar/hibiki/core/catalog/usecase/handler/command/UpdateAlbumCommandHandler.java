package com.ggar.hibiki.core.catalog.usecase.handler.command;

import com.ggar.hibiki.core.catalog.dto.AlbumDto;
import com.ggar.hibiki.core.catalog.dto.UpdateAlbumCommand;
import com.ggar.hibiki.core.catalog.persistence.mapper.AlbumMapper;
import com.ggar.hibiki.core.catalog.port.AlbumRepository;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateAlbumCommandHandler implements CommandHandler<UpdateAlbumCommand, AlbumDto> {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    @Override
    public Mono<AlbumDto> handle(UpdateAlbumCommand command) {
        return albumRepository
                .findById(command.getId())
                .flatMap(album -> {
                    if (command.getTitle() != null) album.setTitle(command.getTitle());
                    if (command.getReleaseYear() != null) album.setReleaseYear(command.getReleaseYear());
                    if (command.getBarcode() != null) album.setBarcode(command.getBarcode());
                    return albumRepository.save(album);
                })
                .map(albumMapper::toDto);
    }
}

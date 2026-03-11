package com.ggar.hibiki.core.catalog.usecase.handler.command;

import com.ggar.hibiki.core.catalog.dto.UpdateSongCommand;
import com.ggar.hibiki.core.catalog.model.Song;
import com.ggar.hibiki.core.catalog.persistence.mapper.SongMapper;
import com.ggar.hibiki.core.catalog.persistence.repository.SongRepository;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateSongCommandHandler implements CommandHandler<UpdateSongCommand, Song> {

    private final SongRepository songRepository;
    private final SongMapper songMapper;

    @Override
    public Mono<Song> handle(UpdateSongCommand command) {
        return songRepository
                .findById(command.getId())
                .flatMap(entity -> {
                    if (command.getTitle() != null) entity.setTitle(command.getTitle());
                    if (command.getFilePath() != null) entity.setFilePath(command.getFilePath());
                    if (command.getDurationMs() != null) entity.setDurationMs(command.getDurationMs());
                    if (command.getTrackNumber() != null) entity.setTrackNumber(command.getTrackNumber());
                    if (command.getIsrc() != null) entity.setIsrc(command.getIsrc());
                    return songRepository.save(entity);
                })
                .map(songMapper::toDomain);
    }
}

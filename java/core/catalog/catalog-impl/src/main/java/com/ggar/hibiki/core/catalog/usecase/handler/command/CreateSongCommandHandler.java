package com.ggar.hibiki.core.catalog.usecase.handler.command;

import com.ggar.hibiki.core.catalog.dto.CreateSongCommand;
import com.ggar.hibiki.core.catalog.dto.SongDto;
import com.ggar.hibiki.core.catalog.persistence.entity.SongEntity;
import com.ggar.hibiki.core.catalog.persistence.mapper.SongMapper;
import com.ggar.hibiki.core.catalog.persistence.repository.AlbumRepository;
import com.ggar.hibiki.core.catalog.persistence.repository.ArtistRepository;
import com.ggar.hibiki.core.catalog.persistence.repository.SongRepository;
import com.ggar.hibiki.core.shared.mediator.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateSongCommandHandler implements CommandHandler<CreateSongCommand, SongDto> {

    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;
    private final SongMapper songMapper;

    @Override
    public Mono<SongDto> handle(CreateSongCommand command) {
        return songRepository
                .findByIsrc(command.getIsrc())
                .switchIfEmpty(Mono.defer(() -> createNewSong(command)))
                .map(songMapper::toDto);
    }

    private Mono<SongEntity> createNewSong(CreateSongCommand command) {
        SongEntity song = new SongEntity();
        song.setTitle(command.getTitle());
        song.setFilePath(command.getFilePath());
        song.setDurationMs(command.getDurationMs());
        song.setTrackNumber(command.getTrackNumber());
        song.setIsrc(command.getIsrc());

        // Resolve Album and Artists sequentially for simplicity
        Mono<SongEntity> withAlbum = command.getAlbumTitle() != null
                        && command.getArtistNames() != null
                        && !command.getArtistNames().isEmpty()
                ? albumRepository
                        .findByTitleIgnoreCaseAndArtistNameIgnoreCase(
                                command.getAlbumTitle(),
                                command.getArtistNames().get(0))
                        .doOnNext(song::setAlbum)
                        .thenReturn(song)
                : Mono.just(song);

        return withAlbum.flatMap(s -> Flux.fromIterable(
                        command.getArtistNames() != null
                                ? command.getArtistNames()
                                : java.util.Collections.<String>emptyList())
                .flatMap(artistRepository::findByNameIgnoreCase)
                .collectList()
                .doOnNext(song::setArtists)
                .then(songRepository.save(song)));
    }
}

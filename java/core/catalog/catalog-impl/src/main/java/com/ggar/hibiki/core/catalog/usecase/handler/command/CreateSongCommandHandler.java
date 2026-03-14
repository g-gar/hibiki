package com.ggar.hibiki.core.catalog.usecase.handler.command;

import com.ggar.hibiki.core.catalog.dto.CreateSongCommand;
import com.ggar.hibiki.core.catalog.dto.SongDto;
import com.ggar.hibiki.core.catalog.model.Song;
import com.ggar.hibiki.core.catalog.persistence.mapper.SongMapper;
import com.ggar.hibiki.core.catalog.port.AlbumRepository;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.core.catalog.port.SongRepository;
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

    private Mono<Song> createNewSong(CreateSongCommand command) {
        Song song = Song.builder()
                .title(command.getTitle())
                .filePath(command.getFilePath())
                .durationMs(command.getDurationMs())
                .trackNumber(command.getTrackNumber())
                .isrc(command.getIsrc())
                .build();

        // Resolve Album and Artists sequentially for simplicity
        Mono<Song> withAlbum = command.getAlbumTitle() != null
                        && command.getArtistNames() != null
                        && !command.getArtistNames().isEmpty()
                ? albumRepository
                        .findByTitleAndArtist(
                                command.getAlbumTitle(),
                                command.getArtistNames().get(0))
                        .doOnNext(song::setAlbum)
                        .thenReturn(song)
                : Mono.just(song);

        return withAlbum.flatMap(s -> Flux.fromIterable(
                        command.getArtistNames() != null
                                ? command.getArtistNames()
                                : java.util.Collections.<String>emptyList())
                .flatMap(artistRepository::findByName)
                .collectList()
                .doOnNext(song::setArtists)
                .then(songRepository.save(song)));
    }
}

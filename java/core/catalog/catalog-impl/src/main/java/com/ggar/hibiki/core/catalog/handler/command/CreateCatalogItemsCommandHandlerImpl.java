package com.ggar.hibiki.core.catalog.handler.command;

import com.ggar.hibiki.core.catalog.model.Album;
import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.catalog.model.Song;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.packages.id3v2.model.Id3v2FrameId;
import com.ggar.hibiki.packages.id3v2.model.Id3v2Tag;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementation of the {@link CreateCatalogItemsCommandHandler}.
 * This service parses ID3 tags and orchestrates the creation of artists, albums, and songs.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateCatalogItemsCommandHandlerImpl implements CreateCatalogItemsCommandHandler {

    private final Mediator mediator;

    @Override
    public Mono<Result> handle(Command command) {
        Id3v2Tag tags = command.id3Tag();

        String title = getTagValue(tags, Id3v2FrameId.TIT2).orElse("Unknown Title");
        String albumTitle = getTagValue(tags, Id3v2FrameId.TALB).orElse("Unknown Album");
        String artistName = getTagValue(tags, Id3v2FrameId.TPE1).orElse("Unknown Artist");
        String isrc = getTagValue(tags, Id3v2FrameId.TSRC).orElse(null);
        String yearStr = getTagValue(tags, Id3v2FrameId.TYER).orElse(null);
        Integer parsedYear = null;
        if (yearStr != null) {
            try {
                parsedYear = Integer.parseInt(yearStr);
            } catch (NumberFormatException e) {
                log.warn("Failed to parse year: {}", yearStr);
            }
        }
        final Integer year = parsedYear;

        String trackNumberStr = getTagValue(tags, Id3v2FrameId.TRCK).orElse(null);
        Integer parsedTrackNumber = null;
        if (trackNumberStr != null) {
            try {
                // Handle formats like "1/10"
                parsedTrackNumber = Integer.parseInt(trackNumberStr.split("/")[0]);
            } catch (NumberFormatException e) {
                log.warn("Failed to parse track number: {}", trackNumberStr);
            }
        }
        final Integer trackNumber = parsedTrackNumber;

        // 1. Create Artist
        CreateArtistCommandHandler.Create artistCmd = new CreateArtistCommandHandler.Create(artistName);

        return Mono.from(mediator.send(artistCmd)).cast(Artist.class).flatMap(artist -> {
            // 2. Create Album
            CreateAlbumCommandHandler.Create albumCmd =
                    new CreateAlbumCommandHandler.Create(albumTitle, year, artist.getName());

            return Mono.from(mediator.send(albumCmd)).cast(Album.class).flatMap(album -> {
                // 3. Create Song
                CreateSongCommandHandler.Create songCmd = new CreateSongCommandHandler.Create(
                        title,
                        null, // filePath not in ID3 tags directly in this context
                        null, // durationMs not in ID3 tags directly
                        trackNumber,
                        isrc,
                        album.getTitle(),
                        List.of(artist.getName()));

                return Mono.from(mediator.send(songCmd))
                        .cast(Song.class)
                        .map(song -> new Result(song.getId(), album.getId()));
            });
        });
    }

    private Optional<String> getTagValue(Id3v2Tag tags, Id3v2FrameId frameId) {
        return tags.getFrames().stream()
                .filter(f -> f.getFrameId() == frameId)
                .findFirst()
                .map(f -> {
                    byte[] data = f.getData();
                    if (data == null || data.length <= 1) return "";
                    // ID3v2.3/4 text frames usually start with an encoding byte.
                    // 0 = ISO-8859-1, 1 = UTF-16, 2 = UTF-16BE, 3 = UTF-8
                    // For simplicity, we skip the first byte and try to decode as UTF-8 (which covers ASCII/ISO-8859-1
                    // often enough)
                    return new String(data, 1, data.length - 1, java.nio.charset.StandardCharsets.UTF_8).trim();
                });
    }
}

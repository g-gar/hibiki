package com.ggar.hibiki.features.metadata.service;

import com.ggar.hibiki.features.metadata.dto.MapToId3Command;
import com.ggar.hibiki.features.metadata.model.Id3Result;
import com.ggar.hibiki.packages.id3v2.model.Id3v2Frame;
import com.ggar.hibiki.packages.id3v2.model.Id3v2FrameId;
import com.ggar.hibiki.packages.id3v2.model.Id3v2Tag;
import com.ggar.hibiki.packages.musicbrainz.model.ArtistCredit;
import com.ggar.hibiki.packages.musicbrainz.model.Recording;
import com.ggar.hibiki.packages.musicbrainz.model.Release;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapToId3CommandHandlerImpl implements MapToId3CommandHandler {

    @Override
    public Publisher<Id3Result> handle(MapToId3Command command) {
        log.info("Mapping metadata to ID3 tags for mediaId: {}", command.getMediaId());

        return Mono.fromCallable(() -> {
            Id3v2Tag tag = new Id3v2Tag();

            if (command.getRawMetadata() == null
                    || command.getRawMetadata().getRecordings() == null
                    || command.getRawMetadata().getRecordings().isEmpty()) {
                log.warn("No recordings found in raw metadata for mediaId: {}", command.getMediaId());
                return Id3Result.builder()
                        .mediaId(command.getMediaId())
                        .tags(tag)
                        .build();
            }

            Recording recording = command.getRawMetadata().getRecordings().get(0);

            // TIT2: Title
            if (recording.getTitle() != null) {
                tag.addFrame(createTextFrame(Id3v2FrameId.TIT2, recording.getTitle()));
            }

            // TPE1: Artist
            if (recording.getArtistCredit() != null
                    && !recording.getArtistCredit().isEmpty()) {
                String artistName = recording.getArtistCredit().stream()
                        .map(ArtistCredit::getName)
                        .collect(Collectors.joining("; "));
                tag.addFrame(createTextFrame(Id3v2FrameId.TPE1, artistName));
            }

            // TSRC: ISRC
            if (command.getRawMetadata().getIsrc() != null) {
                tag.addFrame(createTextFrame(
                        Id3v2FrameId.TSRC, command.getRawMetadata().getIsrc()));
            }

            // Extract Release info (Album, Year, Track Number)
            List<Release> releases = recording.getReleases();
            if (releases != null && !releases.isEmpty()) {
                Release release = releases.get(0);

                // TALB: Album
                if (release.getTitle() != null) {
                    tag.addFrame(createTextFrame(Id3v2FrameId.TALB, release.getTitle()));
                }

                // TYER: Release Year
                if (release.getDate() != null && release.getDate().length() >= 4) {
                    String year = release.getDate().substring(0, 4);
                    tag.addFrame(createTextFrame(Id3v2FrameId.TYER, year));
                }

                // TPE2: Album Artist (Usually the same as Artist for singles, or "Various Artists" for compilations)
                if (release.getArtistCredit() != null
                        && !release.getArtistCredit().isEmpty()) {
                    String albumArtist = release.getArtistCredit().stream()
                            .map(ArtistCredit::getName)
                            .collect(Collectors.joining("; "));
                    tag.addFrame(createTextFrame(Id3v2FrameId.TPE2, albumArtist));
                }
            }

            return Id3Result.builder().mediaId(command.getMediaId()).tags(tag).build();
        });
    }

    private Id3v2Frame createTextFrame(Id3v2FrameId frameId, String text) {
        // ID3v2 text frames start with an encoding byte.
        // 0x00 = ISO-8859-1. 0x01 = UTF-16. 0x03 = UTF-8.
        // We'll use 0x03 UTF-8 for simplicity, though support depends on the standard version.
        byte[] textBytes = text.getBytes(StandardCharsets.UTF_8);
        byte[] data = new byte[textBytes.length + 1];
        data[0] = 0x03; // UTF-8 encoding flag
        System.arraycopy(textBytes, 0, data, 1, textBytes.length);

        return new Id3v2Frame(
                frameId.name(),
                data.length,
                new byte[] {0, 0}, // Flags
                data);
    }
}

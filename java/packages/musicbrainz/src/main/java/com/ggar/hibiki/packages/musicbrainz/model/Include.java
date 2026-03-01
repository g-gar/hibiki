package com.ggar.hibiki.packages.musicbrainz.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines all possible Include subqueries that can be passed to the API.
 * Designed to be used as a bitmask (e.g., Include.RECORDINGS.getMask() |
 * Include.ISRCS.getMask()).
 */
@Getter
@RequiredArgsConstructor
public enum Include {

    // Subqueries
    RECORDINGS(1 << 0, "recordings"),
    RELEASES(1 << 1, "releases"),
    RELEASE_GROUPS(1 << 2, "release-groups"),
    WORKS(1 << 3, "works"),
    ARTISTS(1 << 4, "artists"),
    LABELS(1 << 5, "labels"),
    EVENTS(1 << 6, "events"),
    USER_TAGS(1 << 7, "user-tags"),
    USER_COLLECTIONS(1 << 8, "user-collections"),

    // Relationships
    ARTIST_RELS(1 << 9, "artist-rels"),
    LABEL_RELS(1 << 10, "label-rels"),
    RECORDING_RELS(1 << 11, "recording-rels"),
    RELEASE_RELS(1 << 12, "release-rels"),
    RELEASE_GROUP_RELS(1 << 13, "release-group-rels"),
    URL_RELS(1 << 14, "url-rels"),
    WORK_RELS(1 << 15, "work-rels"),
    INSTRUMENT_RELS(1 << 16, "instrument-rels"),
    PLACE_RELS(1 << 17, "place-rels"),
    SERIES_RELS(1 << 18, "series-rels"),

    // Other inclusions
    ISRCS(1 << 19, "isrcs"),
    PUIDS(1 << 20, "puids"),
    DISCIDS(1 << 21, "discids"),
    MEDIA(1 << 22, "media"),
    ALIASES(1 << 23, "aliases"),
    TAGS(1 << 24, "tags"),
    GENRES(1 << 25, "genres"),
    RATINGS(1 << 26, "ratings"),
    ARTIST_CREDITS(1 << 27, "artist-credits");

    private final long mask;
    private final String value;

    /**
     * Converts a bitmask into a list of MusicBrainz URL string parameters.
     *
     * @param combinedMask the bitwise OR'd mask (e.g., Include.ISRCS.getMask() |
     *                     Include.PUIDS.getMask())
     * @return an array of string values representing the chosen includes
     */
    public static String[] extractIncludes(long combinedMask) {
        List<String> includes = new ArrayList<>();
        for (Include include : values()) {
            if ((combinedMask & include.getMask()) != 0) {
                includes.add(include.getValue());
            }
        }
        return includes.toArray(new String[0]);
    }
}

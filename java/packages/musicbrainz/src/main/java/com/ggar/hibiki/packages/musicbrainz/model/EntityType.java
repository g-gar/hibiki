package com.ggar.hibiki.packages.musicbrainz.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Supported MusicBrainz entity types.
 */
@Getter
@RequiredArgsConstructor
public enum EntityType {
    AREA("area"),
    ARTIST("artist"),
    EVENT("event"),
    GENRE("genre"),
    INSTRUMENT("instrument"),
    LABEL("label"),
    PLACE("place"),
    RECORDING("recording"),
    RELEASE("release"),
    RELEASE_GROUP("release-group"),
    SERIES("series"),
    WORK("work"),
    URL("url");

    private final String value;
}

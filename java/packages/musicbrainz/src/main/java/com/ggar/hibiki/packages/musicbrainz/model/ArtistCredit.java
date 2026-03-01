package com.ggar.hibiki.packages.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a credit for an artist on a recording, release, or track.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtistCredit {
    private String name;
    @JsonProperty("joinphrase")
    private String joinPhrase;
    private Artist artist;
}

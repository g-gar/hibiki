package com.ggar.hibiki.packages.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a Release Group entity.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleaseGroup {
    private String id;
    private String title;
    @JsonProperty("primary-type")
    private String primaryType;
    @JsonProperty("secondary-types")
    private List<String> secondaryTypes;
    @JsonProperty("first-release-date")
    private String firstReleaseDate;
    @JsonProperty("artist-credit")
    private List<ArtistCredit> artistCredit;
    private List<Release> releases;
}

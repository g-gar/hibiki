package com.ggar.hibiki.packages.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Release entity.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Release {
    private String id;
    private String title;
    private String status;
    private String date;
    private String country;
    private String barcode;
    private String quality;
    private String packaging;
    private String asin;

    @JsonProperty("text-language")
    private String textLanguage;

    @JsonProperty("text-script")
    private String textScript;

    @JsonProperty("release-events")
    private List<ReleaseEvent> releaseEvents;

    @JsonProperty("cover-art-archive")
    private CoverArtArchive coverArtArchive;

    @JsonProperty("artist-credit")
    private List<ArtistCredit> artistCredit;

    @JsonProperty("release-group")
    private ReleaseGroup releaseGroup;

    private List<Medium> media;
    private List<Relation> relations;
}

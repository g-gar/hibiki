package com.ggar.hibiki.packages.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a Track inside a Medium.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {
    private String id;
    private Integer position;
    private String number;
    private Integer length;
    private String title;
    @JsonProperty("artist-credit")
    private List<ArtistCredit> artistCredit;
    private Recording recording;
}

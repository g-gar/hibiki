package com.ggar.hibiki.packages.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a Medium inside a Release.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Medium {
    private Integer position;
    private String format;
    @JsonProperty("track-count")
    private Integer trackCount;
    @JsonProperty("track-offset")
    private Integer trackOffset;
    private List<Track> tracks;
}

package com.ggar.hibiki.packages.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Represents a generic relationship in MusicBrainz (e.g., between a Recording
 * and a Work).
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Relation {
    private String type;
    @JsonProperty("type-id")
    private String typeId;
    private String direction;
    @JsonProperty("target-type")
    private String targetType;
    @JsonProperty("target-credit")
    private String targetCredit;
    @JsonProperty("source-credit")
    private String sourceCredit;
    private Map<String, Object> attributes;

    // Depending on the target-type, one of these will be populated
    private Artist artist;
    private Release release;
    private Recording recording;
    private Label label;
    private Work work;
    private Area area;
    @JsonProperty("release-group")
    private ReleaseGroup releaseGroup;
    private Series series;
    private Instrument instrument;
    private Event event;
    private Place place;
    private Url url;
}

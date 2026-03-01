package com.ggar.hibiki.packages.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a Label entity.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Label {
    private String id;
    private String name;
    private String type;
    @JsonProperty("sort-name")
    private String sortName;
    @JsonProperty("label-code")
    private Integer labelCode;
    private String country;
    private Area area;
    private LifeSpan lifespan;
}

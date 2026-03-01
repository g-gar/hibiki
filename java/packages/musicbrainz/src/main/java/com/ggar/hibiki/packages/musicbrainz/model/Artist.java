package com.ggar.hibiki.packages.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents an Artist entity.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Artist {
    private String id;
    private String name;
    @JsonProperty("sort-name")
    private String sortName;
    private String type;
    private String gender;
    private String country;
    private Area area;
    private LifeSpan lifespan;
    @JsonProperty("begin-area")
    private Area beginArea;
    @JsonProperty("end-area")
    private Area endArea;
    private String disambiguation;
    private List<Alias> aliases;
    private List<Tag> tags;
    private List<String> ipis;
    private List<String> isnis;
    private List<Relation> relations;
}

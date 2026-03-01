package com.ggar.hibiki.packages.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a Recording entity.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recording {
    private String id;
    private String title;
    private Integer length;
    private Boolean video;
    private List<String> isrcs;
    @JsonProperty("artist-credit")
    private List<ArtistCredit> artistCredit;
    private List<Release> releases;
    private List<Alias> aliases;
    private List<Tag> tags;
    private List<Relation> relations;
}

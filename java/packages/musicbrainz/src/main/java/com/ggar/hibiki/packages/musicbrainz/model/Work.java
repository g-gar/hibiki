package com.ggar.hibiki.packages.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a Work entity.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Work {
    private String id;
    private String title;
    private String type;
    private String language;
    private List<String> iswcs;
    private List<Alias> aliases;
    private List<Tag> tags;
}

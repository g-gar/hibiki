package com.ggar.hibiki.packages.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a MusicBrainz Area entity.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Area {
    private String id;
    private String name;
    private String type;

    @JsonProperty("sort-name")
    private String sortName;

    @JsonProperty("iso-3166-1-codes")
    private List<String> iso31661Codes;

    private String disambiguation;
}

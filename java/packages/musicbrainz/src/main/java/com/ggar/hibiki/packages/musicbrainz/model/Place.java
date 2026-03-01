package com.ggar.hibiki.packages.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Place entity in MusicBrainz.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Place {
    private String id;
    private String name;
    private String type;
    private String address;
    private Area area;
}

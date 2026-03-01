package com.ggar.hibiki.packages.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a Release Event for a Release entity.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleaseEvent {
    private String date;
    private Area area;
}

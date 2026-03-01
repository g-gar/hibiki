package com.ggar.hibiki.packages.musicbrainz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents Cover Art Archive information for a Release entity.
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoverArtArchive {
    private Boolean artwork;
    private Boolean front;
    private Boolean back;
    private Integer count;
}

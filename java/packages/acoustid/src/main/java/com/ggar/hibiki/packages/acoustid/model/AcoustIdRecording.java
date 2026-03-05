package com.ggar.hibiki.packages.acoustid.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a recording entry within an AcoustID lookup result.
 * Contains the recording's unique identifier and its associated ISRC codes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcoustIdRecording {
    @JsonProperty("id")
    private String id;

    @JsonProperty("isrcs")
    private List<String> isrcs;
}

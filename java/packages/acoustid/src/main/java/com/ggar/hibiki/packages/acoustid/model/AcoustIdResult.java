package com.ggar.hibiki.packages.acoustid.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a single result from the AcoustID API lookup.
 * Contains the match score, identifier, and a list of related recordings.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcoustIdResult {
    @JsonProperty("id")
    private String id;

    @JsonProperty("score")
    private double score;

    @JsonProperty("recordings")
    private List<AcoustIdRecording> recordings;
}

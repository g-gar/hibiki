package com.ggar.hibiki.packages.acoustid.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents the root response from the AcoustID API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcoustIdLookupResponse {
    @JsonProperty("status")
    private String status;

    @JsonProperty("results")
    private List<AcoustIdResult> results;
}

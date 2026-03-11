package com.ggar.hibiki.core.catalog.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    private String id;
    private String title;
    private String filePath;
    private Long durationMs;
    private Integer trackNumber;
    private String isrc;
    private List<Artist> artists;
    private Album album;
}

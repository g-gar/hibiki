package com.ggar.hibiki.core.catalog.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

package com.ggar.hibiki.core.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDto {
    private String id;
    private String title;
    private Integer releaseYear;
    private String barcode;
    private ArtistDto artist;
}

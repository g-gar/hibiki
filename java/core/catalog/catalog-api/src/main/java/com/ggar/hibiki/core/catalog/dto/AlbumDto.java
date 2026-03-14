package com.ggar.hibiki.core.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDto {
    private UUID id;
    private String title;
    private Integer releaseYear;
    private String barcode;
    private ArtistDto artist;
}

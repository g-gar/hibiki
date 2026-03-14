package com.ggar.hibiki.core.catalog.model;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Album {
    private UUID id;
    private String title;
    private Integer releaseYear;
    private String barcode;
    private Artist artist;
}

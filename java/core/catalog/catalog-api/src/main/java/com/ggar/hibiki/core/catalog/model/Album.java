package com.ggar.hibiki.core.catalog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Album {
    private String id;
    private String title;
    private Integer releaseYear;
    private String barcode;
    private Artist artist;
}

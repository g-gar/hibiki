package com.ggar.hibiki.core.catalog.dto;

import com.ggar.hibiki.core.catalog.model.Album;
import com.ggar.hibiki.core.shared.mediator.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAlbumQuery implements Query<Album> {
    private String id;
    private String barcode;
    private String title;
}

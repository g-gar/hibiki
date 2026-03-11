package com.ggar.hibiki.core.catalog.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAlbumCommand implements Command<AlbumDto> {
    private String id;
    private String title;
    private Integer releaseYear;
    private String barcode;
}

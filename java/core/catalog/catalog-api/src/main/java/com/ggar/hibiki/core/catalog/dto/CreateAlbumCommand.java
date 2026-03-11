package com.ggar.hibiki.core.catalog.dto;

import com.ggar.hibiki.core.catalog.model.Album;
import com.ggar.hibiki.core.shared.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlbumCommand implements Command<Album> {
    private String title;
    private Integer releaseYear;
    private String artistName;
}

package com.ggar.hibiki.core.catalog.dto;

import com.ggar.hibiki.core.shared.mediator.Query;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindAlbumQuery implements Query<AlbumDto> {
    private UUID id;
    private String barcode;
    private String title;
}

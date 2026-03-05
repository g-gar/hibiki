package com.ggar.hibiki.core.catalog.model.query;

import com.ggar.hibiki.core.catalog.model.domain.Song;
import com.ggar.hibiki.core.shared.mediator.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindSongQuery implements Query<Song> {
    private String id;
    private String isrc;
}

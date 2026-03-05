package com.ggar.hibiki.core.catalog.model.query;

import com.ggar.hibiki.core.catalog.model.domain.Artist;
import com.ggar.hibiki.core.shared.mediator.Query;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FindArtistQuery implements Query<Artist> {
    private String id;
    private String isni;
    private String name;
}

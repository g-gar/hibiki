package com.ggar.hibiki.core.catalog.model.command;

import com.ggar.hibiki.core.catalog.model.domain.Artist;
import com.ggar.hibiki.core.shared.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateArtistCommand implements Command<Artist> {
    private String id;
    private String name;
    private String isni;
}

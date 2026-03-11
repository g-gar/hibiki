package com.ggar.hibiki.core.catalog.dto;

import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.shared.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateArtistCommand implements Command<Artist> {
    private String name;
}

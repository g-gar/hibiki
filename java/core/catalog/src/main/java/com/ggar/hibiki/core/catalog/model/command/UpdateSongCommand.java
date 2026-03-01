package com.ggar.hibiki.core.catalog.model.command;

import com.ggar.hibiki.core.catalog.model.domain.Song;
import com.ggar.hibiki.core.shared.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSongCommand implements Command<Song> {
    private String id;
    private String title;
    private String filePath;
    private Long durationMs;
    private Integer trackNumber;
    private String isrc;
}

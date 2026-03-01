package com.ggar.hibiki.core.catalog.model.command;

import com.ggar.hibiki.core.catalog.model.domain.Song;
import com.ggar.hibiki.core.shared.mediator.Command;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSongCommand implements Command<Song> {
    private String title;
    private String filePath;
    private Long durationMs;
    private Integer trackNumber;
    private String isrc;
    private String albumTitle;
    private List<String> artistNames;
}

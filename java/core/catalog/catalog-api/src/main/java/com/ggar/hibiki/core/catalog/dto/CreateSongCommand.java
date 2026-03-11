package com.ggar.hibiki.core.catalog.dto;

import com.ggar.hibiki.core.shared.mediator.Command;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSongCommand implements Command<SongDto> {
    private String title;
    private String filePath;
    private Long durationMs;
    private Integer trackNumber;
    private String isrc;
    private String albumTitle;
    private List<String> artistNames;
}

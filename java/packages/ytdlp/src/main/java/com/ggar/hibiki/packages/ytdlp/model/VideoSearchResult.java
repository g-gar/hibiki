package com.ggar.hibiki.packages.ytdlp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VideoSearchResult {
    private String id;
    private String title;
    private String url;
    private String uploader;

    /**
     * Duration in seconds.
     */
    private Double duration;
}

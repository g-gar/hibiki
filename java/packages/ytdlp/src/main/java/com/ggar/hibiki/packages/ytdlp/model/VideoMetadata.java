package com.ggar.hibiki.packages.ytdlp.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class VideoMetadata {
    private String id;
    private String title;
    private String uploader;
    private String description;

    /**
     * Video duration in seconds.
     */
    private Double duration;

    /**
     * The webpage URL pointing to this specific video/audio
     */
    private String webpageUrl;

    /**
     * Indicates whether the media is a live stream.
     */
    private Boolean isLive;

    /**
     * Indicates whether the media is age restricted.
     */
    private Boolean ageRestricted;

    private List<Chapter> chapters;
    private List<MediaFormat> formats;
}

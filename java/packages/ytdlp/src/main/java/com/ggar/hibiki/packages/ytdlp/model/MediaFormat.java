package com.ggar.hibiki.packages.ytdlp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaFormat {
    /**
     * e.g., "140", "251"
     */
    private String formatId;

    /**
     * e.g., "m4a", "webm"
     */
    private String ext;

    /**
     * e.g., "audio only", "1080p"
     */
    private String resolution;

    /**
     * e.g., "av01.0.08M.08", "none"
     */
    private String vcodec;

    /**
     * e.g., "mp4a.40.2", "opus"
     */
    private String acodec;

    /**
     * audio bitrate
     */
    private Double abr;

    /**
     * video bitrate
     */
    private Double vbr;

    /**
     * total bitrate
     */
    private Double tbr;

    /**
     * in bytes
     */
    private Long filesize;
}

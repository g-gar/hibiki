package com.ggar.hibiki.packages.ytdlp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Chapter {
    private String title;

    /**
     * in seconds
     */
    private Double startTime;

    /**
     * in seconds
     */
    private Double endTime;
}

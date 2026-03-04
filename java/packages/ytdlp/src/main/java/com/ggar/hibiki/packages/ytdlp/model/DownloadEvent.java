package com.ggar.hibiki.packages.ytdlp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DownloadEvent {

    public enum EventType {
        STARTING,
        PROGRESS,
        FILE_COMPLETED,
        FINISHED,
        ERROR
    }

    private EventType type;

    /**
     * Optional message, e.g., the exact percentage from stdout, or the error trace.
     */
    private String message;

    /**
     * The estimated download progress percentage between 0.0 and 100.0.
     * Only populated if EventType is PROGRESS.
     */
    private Double progressPercentage;

    /**
     * The path to the file that was written to disk.
     * Populated when EventType is FILE_COMPLETED.
     */
    private String savedFilePath;
}

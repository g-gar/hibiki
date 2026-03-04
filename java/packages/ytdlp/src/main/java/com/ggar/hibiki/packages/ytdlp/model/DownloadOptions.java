package com.ggar.hibiki.packages.ytdlp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DownloadOptions {

    /**
     * E.g., "140", "bestaudio", "251".
     * If not provided, yt-dlp usually defaults to the best audio/video mix.
     */
    private String format;

    /**
     * Whether to tell yt-dlp to extract the audio natively using ffmpeg (e.g.,
     * --extract-audio).
     */
    private boolean extractAudio;

    /**
     * The target audio format if extractAudio is true (e.g., "mp3", "m4a", "wav").
     */
    private String audioFormat;

    /**
     * Directory to output the file to. Defaults to the configured scratchpad if
     * null.
     */
    private String outputDirectory;

    /**
     * Output filename template. E.g., "%(id)s.%(ext)s".
     * Defaults to the base configured output template if null.
     */
    private String outputTemplate;

    /**
     * Custom raw arguments that might be needed in extreme cases
     * like "--cookies-from-browser", "firefox"
     */
    private java.util.List<String> customArgs;
}

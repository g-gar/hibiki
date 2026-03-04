package com.ggar.hibiki.packages.ytdlp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "hibiki.ytdlp")
public class YtDlpProperties {

    /**
     * Path to the yt-dlp executable. Defaults to "yt-dlp" assuming it's in the
     * system PATH.
     */
    private String binaryPath = "yt-dlp";

    /**
     * Default output template for downloaded files.
     * %(title)s.%(ext)s is the yt-dlp default for just the title and extension.
     */
    private String defaultOutputTemplate = "%(id)s.%(ext)s";

    /**
     * Default scratchpad directory for intermediate downloads before they are
     * pushed to MinIO.
     */
    private String scratchpadDirectory = "/tmp/hibiki_ingestion";
}

package com.ggar.hibiki.packages.musicbrainz.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * Configuration properties for the MusicBrainz API client.
 */
@Data
@ConfigurationProperties(prefix = "musicbrainz.api")
public class MusicBrainzProperties {

    /**
     * The base URL for the MusicBrainz API.
     * Defaults to the production v2 API.
     */
    private String url = "https://musicbrainz.org/ws/2/";

    /**
     * The User-Agent required by MusicBrainz.
     * Must identify the application or it may be blocked.
     * Example: "MyApp/1.0.0 ( my@email.com )"
     */
    private String userAgent;

    /**
     * The maximum number of requests per second allowed.
     * MusicBrainz policy strictly limits this to 1 per second.
     */
    private int requestsPerSecond = 1;

    /**
     * Returns the configured delay between requests based on the
     * requests-per-second setting.
     *
     * @return the delay duration
     */
    public Duration getDelayPerRequest() {
        if (requestsPerSecond <= 0) {
            return Duration.ZERO;
        }
        return Duration.ofMillis(1000 / requestsPerSecond);
    }
}

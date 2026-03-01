package com.ggar.hibiki.packages.acoustid.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration properties for the AcoustID module.
 * Binds to properties prefixed with `acoustid.api`.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "acoustid.api")
public class AcoustIdProperties {

    /**
     * The AcoustID API client key.
     */
    private String clientKey;

    /**
     * The AcoustID API URL.
     */
    private String url = "https://api.acoustid.org/v2/lookup";

    /**
     * The User-Agent header to use when querying the AcoustID API.
     */
    private String userAgent = "Hibiki/1.0.0 (https://github.com/hibiki)";

    /**
     * Maximum number of requests allowed per second.
     */
    private int requestsPerSecond = 3;
}

package com.ggar.hibiki.packages.musicbrainz.client;

import com.ggar.hibiki.packages.musicbrainz.config.MusicBrainzProperties;
import com.ggar.hibiki.packages.musicbrainz.logging.Logger;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Reactive wrapper for standard MusicBrainz API calls.
 * Enforces the required rate limiting and HTTP headers (User-Agent, Accept:
 * application/json).
 */
public class MusicBrainzWebClient {

    private final WebClient webClient;
    private final MusicBrainzProperties properties;
    private final Logger logger;

    public MusicBrainzWebClient(WebClient.Builder webClientBuilder, MusicBrainzProperties properties, Logger logger) {
        this.properties = properties;
        this.logger = logger;

        String userAgent = Optional.ofNullable(properties.getUserAgent())
                .orElseThrow(() -> new IllegalArgumentException(
                        "musicbrainz.api.user-agent must be properly configured to avoid being blocked."));

        logger.info("Initializing MusicBrainz WebClient with User-Agent: {}", userAgent);

        this.webClient = webClientBuilder
                .baseUrl(properties.getUrl())
                .defaultHeader(HttpHeaders.USER_AGENT, userAgent)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Executes a GET request to the given URI and maps the response onto the
     * requested type.
     * Enforces the rate-limiting delay configured in the properties.
     *
     * @param uri          the URI to call
     * @param responseType the expected response class
     * @param <T>          the type of the response
     * @return a {@link Mono} emitting the deserialized response body
     */
    public <T> Mono<T> get(String uri, Class<T> responseType) {
        logger.debug("Executing MusicBrainz GET request to: {}", uri);
        return webClient
                .get()
                .uri(uri)
                .retrieve()
                .bodyToMono(responseType)
                .delayElement(properties.getDelayPerRequest());
    }
}

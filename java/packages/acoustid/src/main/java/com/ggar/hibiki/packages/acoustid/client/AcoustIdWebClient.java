package com.ggar.hibiki.packages.acoustid.client;

import com.ggar.hibiki.packages.acoustid.config.AcoustIdProperties;
import com.ggar.hibiki.packages.acoustid.model.AcoustIdLookupResponse;
import java.time.Duration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * WebClient abstraction for querying the AcoustID API.
 * Configures the base URL, default headers, and manages rate limiting
 * as specified in the {@link AcoustIdProperties}.
 */
public class AcoustIdWebClient {

    private final WebClient webClient;
    private final AcoustIdProperties properties;
    private final Duration intervalBetweenRequests;

    /**
     * Constructs a new {@link AcoustIdWebClient}.
     *
     * @param webClientBuilder the builder used to create the inner
     *                         {@link WebClient}
     * @param properties       the AcoustID configuration properties
     */
    public AcoustIdWebClient(WebClient.Builder webClientBuilder, AcoustIdProperties properties) {
        this.properties = properties;
        this.intervalBetweenRequests = Duration.ofMillis(1000 / Math.max(1, properties.getRequestsPerSecond()));

        this.webClient = webClientBuilder
                .baseUrl(properties.getUrl())
                .defaultHeader(HttpHeaders.USER_AGENT, properties.getUserAgent())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    /**
     * Queries the AcoustID API to retrieve MusicBrainz information based on an
     * audio fingerprint.
     * The lookup is rate-limited according to the configuration.
     *
     * @param fingerprint     the calculated audio fingerprint
     * @param durationSeconds the duration of the audio in seconds
     * @return a {@link Mono} emitting the {@link AcoustIdLookupResponse}
     */
    public Mono<AcoustIdLookupResponse> lookupByFingerprint(String fingerprint, int durationSeconds) {
        return Mono.delay(intervalBetweenRequests).flatMap(delay -> this.webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("client", properties.getClientKey())
                        .queryParam("duration", durationSeconds)
                        .queryParam("fingerprint", fingerprint)
                        .queryParam("meta", "recordings+isrcs")
                        .build())
                .retrieve()
                .bodyToMono(AcoustIdLookupResponse.class));
    }
}

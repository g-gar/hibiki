package com.ggar.hibiki.packages.musicbrainz.config;

import com.ggar.hibiki.packages.musicbrainz.DefaultMusicBrainz;
import com.ggar.hibiki.packages.musicbrainz.MusicBrainz;
import com.ggar.hibiki.packages.musicbrainz.client.MusicBrainzWebClient;
import com.ggar.hibiki.packages.musicbrainz.logging.Logger;
import com.ggar.hibiki.packages.musicbrainz.logging.NoOpLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Spring Boot auto-configuration for the MusicBrainz module.
 * Provides the required beans to interact with the MusicBrainz APIs.
 */
@Configuration
@Import(MusicBrainzProperties.class)
public class MusicBrainzConfiguration {

    /**
     * Provides a default silent logger if no custom {@link Logger} is defined by
     * the consuming application.
     *
     * @return an instance of {@link NoOpLogger}
     */
    @Bean
    @ConditionalOnMissingBean(Logger.class)
    public Logger musicBrainzLogger() {
        return new NoOpLogger();
    }

    /**
     * Provides the reactive web client for MusicBrainz.
     *
     * @param builder    the pre-configured web client builder
     * @param properties the MusicBrainz properties
     * @param logger     the configured logger implementation
     * @return an instance of {@link MusicBrainzWebClient}
     */
    @Bean
    public MusicBrainzWebClient musicBrainzWebClient(
            WebClient.Builder builder, MusicBrainzProperties properties, Logger logger) {
        return new MusicBrainzWebClient(builder, properties, logger);
    }

    /**
     * Provides the main entry point for the MusicBrainz library.
     *
     * @param webClient the configured reactive web client
     * @return an instance of {@link DefaultMusicBrainz}
     */
    @Bean
    public MusicBrainz musicBrainz(MusicBrainzWebClient webClient) {
        return new DefaultMusicBrainz(webClient);
    }
}

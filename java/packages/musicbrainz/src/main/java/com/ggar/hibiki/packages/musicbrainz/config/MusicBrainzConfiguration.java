package com.ggar.hibiki.packages.musicbrainz.config;

import com.ggar.hibiki.packages.musicbrainz.DefaultMusicBrainz;
import com.ggar.hibiki.packages.musicbrainz.MusicBrainz;
import com.ggar.hibiki.packages.musicbrainz.client.MusicBrainzWebClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Spring Boot auto-configuration for the MusicBrainz module.
 * Provides the required beans to interact with the MusicBrainz APIs.
 */
@Configuration
@Import(MusicBrainzProperties.class)
public class MusicBrainzConfiguration {

    /**
     * Provides the reactive web client for MusicBrainz.
     *
     * @param builder    the pre-configured web client builder
     * @param properties the MusicBrainz properties
     * @Bean
     * public MusicBrainzWebClient musicBrainzWebClient(
     * WebClient.Builder builder, MusicBrainzProperties properties) {
     * return new MusicBrainzWebClient(builder, properties);
     * }
     *
     * /**
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

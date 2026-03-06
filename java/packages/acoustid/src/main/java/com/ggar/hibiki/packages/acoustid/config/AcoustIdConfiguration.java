package com.ggar.hibiki.packages.acoustid.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggar.hibiki.packages.acoustid.AcoustId;
import com.ggar.hibiki.packages.acoustid.DefaultAcoustId;
import com.ggar.hibiki.packages.acoustid.client.AcoustIdWebClient;
import com.ggar.hibiki.packages.acoustid.fpcalc.FingerprintCalculator;
import com.ggar.hibiki.packages.acoustid.fpcalc.FpcalcFingerprintCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Spring Boot auto-configuration for the AcoustID module.
 * Provides the required beans to interact with the AcoustID API and `fpcalc`.
 */
@Configuration
@Import(AcoustIdProperties.class)
public class AcoustIdConfiguration {

    /**
     * Provides the {@link FingerprintCalculator} implementation.
     *
     * @param objectMapper the Jackson object mapper for JSON parsing
     * @param logger       the configured application logger
     * @return a configured {@link FpcalcFingerprintCalculator}
     */
    @Bean
    public FingerprintCalculator fingerprintCalculator(ObjectMapper objectMapper) {
        return new FpcalcFingerprintCalculator(objectMapper);
    }

    /**
     * Provides the {@link AcoustIdWebClient} to execute HTTP queries.
     *
     * @param webClientBuilder the web client builder
     * @param properties       the properties containing API keys and endpoints
     * @return a configured {@link AcoustIdWebClient}
     */
    @Bean
    public AcoustIdWebClient acoustIdWebClient(WebClient.Builder webClientBuilder, AcoustIdProperties properties) {
        return new AcoustIdWebClient(webClientBuilder, properties);
    }

    /**
     * Provides the {@link AcoustId} which serves as the main entry point to
     * this module.
     *
     * @param fingerprintCalculator the component responsible for generating the
     *                              audio fingerprint
     * @param acoustIdWebClient     the component responsible for fetching from the
     *                              AcoustID API
     * @return a configured {@link DefaultAcoustId}
     */
    @Bean
    public AcoustId acoustId(FingerprintCalculator fingerprintCalculator, AcoustIdWebClient acoustIdWebClient) {
        return new DefaultAcoustId(fingerprintCalculator, acoustIdWebClient);
    }
}

package com.ggar.hibiki.packages.jwt.config;

import com.ggar.hibiki.packages.jwt.logging.Logger;
import com.ggar.hibiki.packages.jwt.logging.NoOpLogger;
import com.ggar.hibiki.packages.jwt.signer.JjwtSigner;
import com.ggar.hibiki.packages.jwt.signer.JwtSigner;
import com.ggar.hibiki.packages.jwt.verifier.JjwtVerifier;
import com.ggar.hibiki.packages.jwt.verifier.JwtVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class to instantiate the default signer, verifier, and
 * logger beans automatically.
 */
@Configuration
public class JwtConfiguration {

    /**
     * Provides a default no-operation logger if no other {@link Logger} bean is
     * defined in the application context.
     *
     * @return a {@link NoOpLogger} instance
     */
    @Bean
    @ConditionalOnMissingBean(Logger.class)
    public Logger defaultJwtLogger() {
        return new NoOpLogger();
    }

    /**
     * Provides a {@link JwtSigner} bean configured with the application secret and
     * expiration time.
     *
     * @param secret         the JWT signing secret
     * @param expirationTime the token expiration time in milliseconds
     * @param logger         the logger instance
     * @return a configured {@link JwtSigner}
     */
    @Bean
    public JwtSigner jwtSigner(
            @Value("${hibiki.security.jwt.secret:defaultSecretForJwtGenerationMin256b}") String secret,
            @Value("${hibiki.security.jwt.expiration:3600000}") long expirationTime,
            Logger logger) {
        return new JjwtSigner(secret, expirationTime, logger);
    }

    /**
     * Provides a {@link JwtVerifier} bean configured with the application secret.
     *
     * @param secret the JWT verification secret
     * @param logger the logger instance
     * @return a configured {@link JwtVerifier}
     */
    @Bean
    public JwtVerifier jwtVerifier(
            @Value("${hibiki.security.jwt.secret:defaultSecretForJwtGenerationMin256b}") String secret,
            Logger logger) {
        return new JjwtVerifier(secret, logger);
    }
}

package com.ggar.hibiki.packages.otp.config;

import com.ggar.hibiki.packages.otp.generator.StandardTotpGenerator;
import com.ggar.hibiki.packages.otp.generator.TotpGenerator;
import com.ggar.hibiki.packages.otp.logging.Logger;
import com.ggar.hibiki.packages.otp.logging.NoOpLogger;
import com.ggar.hibiki.packages.otp.util.Base32SecretEncoder;
import com.ggar.hibiki.packages.otp.util.SecretEncoder;
import com.ggar.hibiki.packages.otp.verifier.StandardTotpVerifier;
import com.ggar.hibiki.packages.otp.verifier.TotpVerifier;
import java.security.SecureRandom;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration class to instantiate the default generator, verifier,
 * and logger beans automatically.
 */
@Configuration
public class OtpConfiguration {

    @Bean
    @ConditionalOnMissingBean(Logger.class)
    public Logger defaultOtpLogger() {
        return new NoOpLogger();
    }

    @Bean
    @ConditionalOnMissingBean(SecureRandom.class)
    public SecureRandom secureRandom() {
        return new SecureRandom();
    }

    @Bean
    @ConditionalOnMissingBean(SecretEncoder.class)
    public SecretEncoder secretEncoder(
            @Value("${hibiki.security.otp.base32.alphabet:" + Base32SecretEncoder.DEFAULT_ALPHABET + "}")
                    String alphabet) {
        return new Base32SecretEncoder(alphabet);
    }

    @Bean
    public TotpGenerator totpGenerator(
            SecureRandom secureRandom,
            Logger logger,
            SecretEncoder secretEncoder,
            @Value("${hibiki.security.otp.secretLengthBytes:20}") int secretLengthBytes,
            @Value("${hibiki.security.otp.issuer:Hibiki}") String issuer,
            @Value("${hibiki.security.otp.algorithm:SHA1}") String algorithm,
            @Value("${hibiki.security.otp.digits:6}") int digits,
            @Value("${hibiki.security.otp.period:30}") int period) {
        return new StandardTotpGenerator(
                secureRandom, logger, secretEncoder, secretLengthBytes, issuer, algorithm, digits, period);
    }

    @Bean
    public TotpVerifier totpVerifier(
            Logger logger,
            SecretEncoder secretEncoder,
            @Value("${hibiki.security.otp.algorithm:SHA1}") String algorithm,
            @Value("${hibiki.security.otp.digits:6}") int digits,
            @Value("${hibiki.security.otp.period:30}") int period,
            @Value("${hibiki.security.otp.windowSize:1}") int windowSize) {
        return new StandardTotpVerifier(logger, secretEncoder, algorithm, digits, period, windowSize);
    }
}

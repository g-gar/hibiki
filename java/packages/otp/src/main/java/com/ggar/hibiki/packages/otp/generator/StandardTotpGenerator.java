package com.ggar.hibiki.packages.otp.generator;

import com.ggar.hibiki.packages.otp.exception.OtpException;
import com.ggar.hibiki.packages.otp.logging.Logger;
import com.ggar.hibiki.packages.otp.util.SecretEncoder;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * Concrete reactive implementation of {@link TotpGenerator}.
 */
public class StandardTotpGenerator implements TotpGenerator {

    private final SecureRandom secureRandom;
    private final Logger logger;
    private final SecretEncoder secretEncoder;
    private final int secretLengthBytes;
    private final String issuer;
    private final String algorithm;
    private final int digits;
    private final int period;

    /**
     * Constructs a new {@link StandardTotpGenerator}.
     *
     * @param secureRandom      the secure random instance to use
     * @param logger            the logger instance
     * @param secretEncoder     the encoder instance to use for the secret
     * @param secretLengthBytes the desired length of the secret in bytes (before
     *                          encoding)
     * @param issuer            the name of the application/issuer
     * @param algorithm         the expected algorithm (e.g., SHA1, SHA256)
     * @param digits            the number of digits in the OTP
     * @param period            the time period in seconds
     */
    public StandardTotpGenerator(SecureRandom secureRandom, Logger logger, SecretEncoder secretEncoder,
            int secretLengthBytes, String issuer, String algorithm, int digits, int period) {
        this.secureRandom = secureRandom;
        this.logger = logger;
        this.secretEncoder = secretEncoder;
        this.secretLengthBytes = secretLengthBytes;
        this.issuer = issuer;
        this.algorithm = algorithm;
        this.digits = digits;
        this.period = period;
    }

    @Override
    public Mono<String> generateSecret() {
        return Mono.fromCallable(() -> {
            try {
                logger.debug("Generating new TOTP secret of {} bytes", secretLengthBytes);
                byte[] secretBytes = new byte[secretLengthBytes];
                secureRandom.nextBytes(secretBytes);
                return secretEncoder.encode(secretBytes);
            } catch (Exception e) {
                logger.error("Failed to generate TOTP secret", e);
                throw new OtpException("Failed to generate TOTP secret", e);
            }
        });
    }

    @Override
    public Mono<String> generateUri(String secret, String accountName) {
        return Mono.fromCallable(() -> {
            try {
                logger.debug("Generating otpauth:// URI for account: {}", accountName);
                String encodedIssuer = URLEncoder.encode(issuer, StandardCharsets.UTF_8.toString()).replace("+", "%20");
                String encodedAccount = URLEncoder.encode(accountName, StandardCharsets.UTF_8.toString()).replace("+",
                        "%20");

                return String.format(
                        "otpauth://totp/%s:%s?secret=%s&issuer=%s&algorithm=%s&digits=%d&period=%d",
                        encodedIssuer,
                        encodedAccount,
                        secret,
                        encodedIssuer,
                        algorithm,
                        digits,
                        period);
            } catch (Exception e) {
                logger.error("Failed to generate OTP URI", e);
                throw new OtpException("Failed to generate OTP URI", e);
            }
        });
    }
}

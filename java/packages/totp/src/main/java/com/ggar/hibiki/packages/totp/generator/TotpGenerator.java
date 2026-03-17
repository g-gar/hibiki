package com.ggar.hibiki.packages.totp.generator;

import reactor.core.publisher.Mono;

/**
 * Interface defining the contract for generating a new random TOTP secret and
 * formatting it.
 */
public interface TotpGenerator {

    /**
     * Generates a new cryptographically secure random TOTP secret in Base32.
     *
     * @return a Mono emitting the new Base32-encoded secret
     */
    Mono<String> generateSecret();

    /**
     * Generates an otpauth:// URI for the given secret and account details,
     * which can be used to generate a QR code for authenticator apps.
     *
     * @param secret      the Base32 encoded secret
     * @param accountName the name of the user's account (e.g., user@example.com)
     * @return a Mono emitting the otpauth:// URI string
     */
    Mono<String> generateUri(String secret, String accountName);
}

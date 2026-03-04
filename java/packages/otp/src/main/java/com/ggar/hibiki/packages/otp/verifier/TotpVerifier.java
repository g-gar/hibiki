package com.ggar.hibiki.packages.otp.verifier;

import reactor.core.publisher.Mono;

/**
 * Interface defining the contract for validating a TOTP code against a secret.
 */
public interface TotpVerifier {

    /**
     * Verifies the provided 6-digit TOTP code against the user's secret.
     * Accounts for time drift windows.
     *
     * @param secret the Base32-encoded user secret
     * @param code   the 6-digit (or N-digit) code provided by the user
     * @return a Mono emitting true if the code is valid, false otherwise
     */
    Mono<Boolean> verify(String secret, String code);
}

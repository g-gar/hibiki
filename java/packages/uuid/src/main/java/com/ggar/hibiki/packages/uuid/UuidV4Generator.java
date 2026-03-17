package com.ggar.hibiki.packages.uuid;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * Generates UUID Version 4 (Random).
 * <p>
 * This implementation uses SecureRandom to generate 16 bytes.
 * It manually sets the version (4) and variant (2) bits according to RFC 4122,
 * avoiding reliance on the default java.util.UUID.randomUUID() algorithm.
 */
public final class UuidV4Generator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private UuidV4Generator() {
        // Utility class
    }

    /**
     * Generates a new UUID v4.
     *
     * @return A newly generated UUID v4.
     */
    public static UUID generate() {
        byte[] randomBytes = new byte[16];
        SECURE_RANDOM.nextBytes(randomBytes);

        // Set version to 4 (0100)
        randomBytes[6] &= 0x0f; // clear version
        randomBytes[6] |= 0x40; // set to version 4

        // Set variant to RFC 4122 (10)
        randomBytes[8] &= 0x3f; // clear variant
        randomBytes[8] |= (byte) 0x80; // set to RFC variant

        long msb = 0;
        long lsb = 0;
        for (int i = 0; i < 8; i++) {
            msb = (msb << 8) | (randomBytes[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            lsb = (lsb << 8) | (randomBytes[i] & 0xff);
        }

        return new UUID(msb, lsb);
    }
}

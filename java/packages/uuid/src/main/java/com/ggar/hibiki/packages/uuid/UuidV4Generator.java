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
public class UuidV4Generator implements UuidGenerator {

    private final SecureRandom secureRandom;

    public UuidV4Generator() {
        this.secureRandom = new SecureRandom();
    }

    public UuidV4Generator(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    @Override
    public UUID generate() {
        byte[] randomBytes = new byte[16];
        secureRandom.nextBytes(randomBytes);

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

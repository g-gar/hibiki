package com.ggar.hibiki.packages.uuid;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;

/**
 * Generates UUID Version 7 (Time-ordered).
 * <p>
 * This implementation generates a UUID based on the Unix epoch timestamp in
 * milliseconds,
 * followed by 74 bits of random data. This guarantees chronological sorting in
 * databases
 * while maintaining the collision resistance of UUIDv4.
 */
public class UuidV7Generator implements UuidGenerator {

    private final SecureRandom secureRandom;

    public UuidV7Generator() {
        this.secureRandom = new SecureRandom();
    }

    public UuidV7Generator(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;
    }

    @Override
    public UUID generate() {
        // 48-bit Unix timestamp in milliseconds
        long timestampMs = Instant.now().toEpochMilli() & 0xFFFFFFFFFFFFL;

        // Generate 10 random bytes for the random part
        byte[] randomBytes = new byte[10];
        secureRandom.nextBytes(randomBytes);

        // Build most significant bits (MSB)
        // 48 bits timestamp | 4 bits version | 12 bits random_a
        long msb = (timestampMs << 16);

        long randA = ((randomBytes[0] & 0xFFL) << 8) | (randomBytes[1] & 0xFFL);
        randA &= 0x0FFFL; // 12 bits

        msb |= 0x7000L; // Set Version 7 (0111)
        msb |= randA;

        // Build least significant bits (LSB)
        // 2 bits variant | 62 bits random_b
        long lsb = 0;
        for (int i = 2; i < 10; i++) {
            lsb = (lsb << 8) | (randomBytes[i] & 0xFFL);
        }

        lsb &= 0x3FFFFFFFFFFFFFFFL; // Clear top 2 bits
        lsb |= 0x8000000000000000L; // Set Variant RFC 4122 (10)

        return new UUID(msb, lsb);
    }
}

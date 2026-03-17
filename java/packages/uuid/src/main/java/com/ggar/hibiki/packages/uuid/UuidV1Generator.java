package com.ggar.hibiki.packages.uuid;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Generates UUID Version 1 (Time-based).
 * <p>
 * This implementation generates a timestamp based on 100-ns intervals since
 * the Gregorian epoch (1582-10-15). To prevent disclosing hardware MAC
 * addresses,
 * a random 48-bit multicast node ID is generated once.
 */
public final class UuidV1Generator {

    // Offset from Unix epoch (1970-01-01) to UUID epoch (1582-10-15) in 100-ns
    // intervals.
    private static final long EPOCH_OFFSET = 122192928000000000L;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final long NODE;
    private static final int CLOCK_SEQUENCE;
    private static final AtomicLong LAST_TIMESTAMP = new AtomicLong();

    static {
        byte[] nodeBytes = new byte[6];
        SECURE_RANDOM.nextBytes(nodeBytes);
        nodeBytes[0] |= 0x01;

        long n = 0;
        for (int i = 0; i < 6; i++) {
            n = (n << 8) | (nodeBytes[i] & 0xff);
        }
        NODE = n;
        CLOCK_SEQUENCE = SECURE_RANDOM.nextInt() & 0x3FFF;
    }

    private UuidV1Generator() {
        // Utility class
    }

    /**
     * Generates a new UUID v1.
     *
     * @return A newly generated UUID v1.
     */
    public static UUID generate() {
        long timestamp = getUniqueTimestamp();

        // 60-bit timestamp
        long timeLow = timestamp & 0xFFFFFFFFL;
        long timeMid = (timestamp >>> 32) & 0xFFFFL;
        long timeHiAndVersion = (timestamp >>> 48) & 0x0FFFL;

        // Set Version 1
        timeHiAndVersion |= 0x1000L;

        long msb = (timeLow << 32) | (timeMid << 16) | timeHiAndVersion;

        // 14-bit clock sequence + 2-bit variant
        long clockSeqHiAndReserved = (CLOCK_SEQUENCE >>> 8) & 0x3FL;
        long clockSeqLow = CLOCK_SEQUENCE & 0xFFL;

        // Set Variant RFC 4122
        clockSeqHiAndReserved |= 0x80L;

        long lsb = (clockSeqHiAndReserved << 56) | (clockSeqLow << 48) | NODE;

        return new UUID(msb, lsb);
    }

    private static synchronized long getUniqueTimestamp() {
        while (true) {
            Instant now = Instant.now();
            long currentTimestamp = (now.getEpochSecond() * 10000000L) + (now.getNano() / 100) + EPOCH_OFFSET;

            long last = LAST_TIMESTAMP.get();
            if (currentTimestamp > last) {
                if (LAST_TIMESTAMP.compareAndSet(last, currentTimestamp)) {
                    return currentTimestamp;
                }
            } else {
                if (LAST_TIMESTAMP.compareAndSet(last, last + 1)) {
                    return last + 1;
                }
            }
        }
    }
}

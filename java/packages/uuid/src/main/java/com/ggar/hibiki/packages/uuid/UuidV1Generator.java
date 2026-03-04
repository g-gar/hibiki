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
 * a random 48-bit multicast node ID is generated once per instance.
 */
public class UuidV1Generator implements UuidGenerator {

    // Offset from Unix epoch (1970-01-01) to UUID epoch (1582-10-15) in 100-ns
    // intervals.
    private static final long EPOCH_OFFSET = 122192928000000000L;

    private final SecureRandom secureRandom;
    private final long node;
    private final int clockSequence;

    // To ensure uniqueness within the same millisecond
    private final AtomicLong lastTimestamp = new AtomicLong();

    public UuidV1Generator() {
        this(new SecureRandom());
    }

    public UuidV1Generator(SecureRandom secureRandom) {
        this.secureRandom = secureRandom;

        // Generate random 48-bit node ID with multicast bit (least significant bit of
        // first octet) set to 1
        // Avoids MAC address exposure as per RFC 4122 Section 4.5
        byte[] nodeBytes = new byte[6];
        secureRandom.nextBytes(nodeBytes);
        nodeBytes[0] |= 0x01;

        long n = 0;
        for (int i = 0; i < 6; i++) {
            n = (n << 8) | (nodeBytes[i] & 0xff);
        }
        this.node = n;

        // Random 14-bit clock sequence
        this.clockSequence = secureRandom.nextInt() & 0x3FFF;
    }

    @Override
    public UUID generate() {
        long timestamp = getUniqueTimestamp();

        // 60-bit timestamp
        long timeLow = timestamp & 0xFFFFFFFFL;
        long timeMid = (timestamp >>> 32) & 0xFFFFL;
        long timeHiAndVersion = (timestamp >>> 48) & 0x0FFFL;

        // Set Version 1
        timeHiAndVersion |= 0x1000L;

        long msb = (timeLow << 32) | (timeMid << 16) | timeHiAndVersion;

        // 14-bit clock sequence + 2-bit variant
        long clockSeqHiAndReserved = (clockSequence >>> 8) & 0x3FL;
        long clockSeqLow = clockSequence & 0xFFL;

        // Set Variant RFC 4122
        clockSeqHiAndReserved |= 0x80L;

        long lsb = (clockSeqHiAndReserved << 56) | (clockSeqLow << 48) | node;

        return new UUID(msb, lsb);
    }

    private synchronized long getUniqueTimestamp() {
        while (true) {
            Instant now = Instant.now();
            // Convert to 100-ns intervals since 1582-10-15
            // 1 ms = 10,000 * 100-ns intervals
            // 1 ns = 1/100 of a 100-ns interval
            long currentTimestamp = (now.getEpochSecond() * 10000000L) + (now.getNano() / 100) + EPOCH_OFFSET;

            long last = lastTimestamp.get();
            if (currentTimestamp > last) {
                if (lastTimestamp.compareAndSet(last, currentTimestamp)) {
                    return currentTimestamp;
                }
            } else {
                // Time didn't advance or went backwards, just increment the last timestamp
                if (lastTimestamp.compareAndSet(last, last + 1)) {
                    return last + 1;
                }
            }
        }
    }
}

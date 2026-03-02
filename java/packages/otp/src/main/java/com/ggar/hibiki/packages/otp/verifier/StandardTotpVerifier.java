package com.ggar.hibiki.packages.otp.verifier;

import com.ggar.hibiki.packages.otp.exception.OtpException;
import com.ggar.hibiki.packages.otp.logging.Logger;
import com.ggar.hibiki.packages.otp.util.SecretEncoder;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

/**
 * Concrete reactive implementation of {@link TotpVerifier} based on RFC 6238
 * using BouncyCastle.
 */
public class StandardTotpVerifier implements TotpVerifier {

    private final Logger logger;
    private final SecretEncoder secretEncoder;
    private final String algorithm;
    private final int digits;
    private final int period;
    private final int windowSize;

    /**
     * Constructs a new {@link StandardTotpVerifier}.
     *
     * @param logger        the logger instance
     * @param secretEncoder the secret encoder instance
     * @param algorithm     the algorithm to use (e.g., "SHA1", "SHA256", "SHA512")
     * @param digits        the number of digits in the OTP
     * @param period        the time period in seconds
     * @param windowSize    the number of time steps (before and after) to check to
     *                      allow for clock drift
     */
    public StandardTotpVerifier(Logger logger, SecretEncoder secretEncoder, String algorithm, int digits, int period,
            int windowSize) {
        this.logger = logger;
        this.secretEncoder = secretEncoder;
        this.algorithm = algorithm;
        this.digits = digits;
        this.period = period;
        this.windowSize = windowSize;
    }

    @Override
    public Mono<Boolean> verify(String secret, String code) {
        return Mono.fromCallable(() -> {
            try {
                if (code == null || code.length() != digits) {
                    logger.debug("Provided code length {} does not match expected digits {}",
                            code != null ? code.length() : 0, digits);
                    return false;
                }

                long currentBucket = Math.floorDiv(System.currentTimeMillis() / 1000L, period);
                byte[] decodedSecret = secretEncoder.decode(secret);

                for (int i = -windowSize; i <= windowSize; i++) {
                    String generatedCode = generateTotp(decodedSecret, currentBucket + i);
                    if (generatedCode.equals(code)) {
                        logger.debug("Code verified successfully at window offset {}", i);
                        return true;
                    }
                }

                logger.debug("Code verification failed after checking {} window offsets", windowSize);
                return false;
            } catch (Exception e) {
                logger.error("Failed to verify TOTP code", e);
                throw new OtpException("Failed to verify TOTP code", e);
            }
        });
    }

    private String generateTotp(byte[] secret, long time) {
        Mac hmac = getMacAlgorithm();
        hmac.init(new KeyParameter(secret));

        byte[] timeBuffer = ByteBuffer.allocate(8).putLong(time).array();
        hmac.update(timeBuffer, 0, timeBuffer.length);

        byte[] hash = new byte[hmac.getMacSize()];
        hmac.doFinal(hash, 0);

        int offset = hash[hash.length - 1] & 0xf;
        int binary = ((hash[offset] & 0x7f) << 24) |
                ((hash[offset + 1] & 0xff) << 16) |
                ((hash[offset + 2] & 0xff) << 8) |
                (hash[offset + 3] & 0xff);

        int otp = binary % (int) Math.pow(10, digits);

        // Zero-pad to ensure the code has the required number of digits
        String result = Integer.toString(otp);
        while (result.length() < digits) {
            result = "0" + result;
        }
        return result;
    }

    private Mac getMacAlgorithm() {
        if ("SHA256".equalsIgnoreCase(algorithm)) {
            return new HMac(new SHA256Digest());
        } else if ("SHA512".equalsIgnoreCase(algorithm)) {
            return new HMac(new SHA512Digest());
        } else {
            return new HMac(new SHA1Digest());
        }
    }
}

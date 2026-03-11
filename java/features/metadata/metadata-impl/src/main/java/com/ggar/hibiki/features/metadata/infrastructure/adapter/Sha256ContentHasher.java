package com.ggar.hibiki.features.metadata.infrastructure.adapter;

import com.ggar.hibiki.features.metadata.port.ContentHasher;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.springframework.stereotype.Component;

/**
 * SHA-256 ContentHasher implementation supporting progressive hashing.
 */
@Component
public class Sha256ContentHasher implements ContentHasher {

    private static class Sha256State implements HashState {
        final MessageDigest digest;

        Sha256State(MessageDigest digest) {
            this.digest = digest;
        }
    }

    @Override
    public HashState init() {
        try {
            return new Sha256State(MessageDigest.getInstance("SHA-256"));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    @Override
    public HashState update(HashState state, byte[] data) {
        ((Sha256State) state).digest.update(data);
        return state;
    }

    @Override
    public String finalize(HashState state) {
        byte[] hash = ((Sha256State) state).digest.digest();
        StringBuilder sb = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

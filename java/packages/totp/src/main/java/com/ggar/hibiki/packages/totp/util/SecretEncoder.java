package com.ggar.hibiki.packages.totp.util;

/**
 * Interface defining the contract for encoding and decoding TOTP secrets.
 * Allows consumers to provide their own encoding strategies or custom
 * alphabets.
 */
public interface SecretEncoder {

    /**
     * Encodes a byte array into a string representation.
     *
     * @param data the byte array to encode
     * @return the encoded string
     */
    String encode(byte[] data);

    /**
     * Decodes an encoded string back into a byte array.
     *
     * @param encoded the encoded string
     * @return the decoded byte array
     * @throws IllegalArgumentException if the string contains invalid characters
     */
    byte[] decode(String encoded);
}

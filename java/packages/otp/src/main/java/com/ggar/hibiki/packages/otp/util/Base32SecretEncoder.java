package com.ggar.hibiki.packages.otp.util;

/**
 * A configurable Base32 encoder and decoder according to RFC 4648.
 * Authenticator apps require the secret to be Base32-encoded.
 * By using this class, the consumer can specify a custom 32-character alphabet
 * if needed.
 */
public class Base32SecretEncoder implements SecretEncoder {

    public static final String DEFAULT_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567";

    private final char[] alphabet;
    private final int[] decodeTable;

    /**
     * Constructs a Base32SecretEncoder using the standard RFC 4648 alphabet.
     */
    public Base32SecretEncoder() {
        this(DEFAULT_ALPHABET);
    }

    /**
     * Constructs a Base32SecretEncoder with a custom alphabet.
     *
     * @param customAlphabet a 32-character string representing the custom alphabet
     */
    public Base32SecretEncoder(String customAlphabet) {
        if (customAlphabet == null || customAlphabet.length() != 32) {
            throw new IllegalArgumentException("Base32 alphabet must be exactly 32 characters long");
        }
        this.alphabet = customAlphabet.toCharArray();
        this.decodeTable = new int[128];
        for (int i = 0; i < decodeTable.length; i++) {
            decodeTable[i] = -1;
        }
        for (int i = 0; i < alphabet.length; i++) {
            // Ensure characters are within valid ASCII range for the decode table
            if (alphabet[i] >= 128) {
                throw new IllegalArgumentException("Alphabet characters must be valid ASCII");
            }
            decodeTable[alphabet[i]] = i;
        }
    }

    @Override
    public String encode(byte[] data) {
        if (data == null || data.length == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder((data.length * 8 + 4) / 5);
        int buffer = data[0];
        int next = 1;
        int bitsLeft = 8;

        while (bitsLeft > 0 || next < data.length) {
            if (bitsLeft < 5) {
                if (next < data.length) {
                    buffer <<= 8;
                    buffer |= (data[next++] & 0xFF);
                    bitsLeft += 8;
                } else {
                    int pad = 5 - bitsLeft;
                    buffer <<= pad;
                    bitsLeft += pad;
                }
            }
            int index = 0x1F & (buffer >> (bitsLeft - 5));
            bitsLeft -= 5;
            result.append(alphabet[index]);
        }

        return result.toString();
    }

    @Override
    public byte[] decode(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return new byte[0];
        }

        encoded = encoded.toUpperCase().replaceAll("=", "");
        byte[] result = new byte[encoded.length() * 5 / 8];
        int buffer = 0;
        int next = 0;
        int bitsLeft = 0;

        for (char c : encoded.toCharArray()) {
            if (c >= 128 || decodeTable[c] == -1) {
                throw new IllegalArgumentException("Invalid Base32 character: " + c);
            }
            buffer <<= 5;
            buffer |= decodeTable[c] & 0x1F;
            bitsLeft += 5;

            if (bitsLeft >= 8) {
                result[next++] = (byte) (buffer >> (bitsLeft - 8));
                bitsLeft -= 8;
            }
        }

        return result;
    }
}

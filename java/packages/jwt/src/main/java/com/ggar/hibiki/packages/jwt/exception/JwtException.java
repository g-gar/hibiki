package com.ggar.hibiki.packages.jwt.exception;

/**
 * Base exception class to abstract JWT specific library exceptions.
 */
public class JwtException extends RuntimeException {

    public JwtException(String message) {
        super(message);
    }

    public JwtException(String message, Throwable cause) {
        super(message, cause);
    }
}

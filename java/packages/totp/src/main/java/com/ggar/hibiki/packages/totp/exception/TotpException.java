package com.ggar.hibiki.packages.totp.exception;

/**
 * Base exception class to abstract OTP specific errors.
 */
public class TotpException extends RuntimeException {

    public TotpException(String message) {
        super(message);
    }

    public TotpException(String message, Throwable cause) {
        super(message, cause);
    }
}

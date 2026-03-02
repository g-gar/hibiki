package com.ggar.hibiki.packages.otp.exception;

/**
 * Base exception class to abstract OTP specific errors.
 */
public class OtpException extends RuntimeException {

    public OtpException(String message) {
        super(message);
    }

    public OtpException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.ggar.hibiki.packages.otp.logging;

/**
 * Abstract logger interface for the OTP module.
 * Allows consumers to provide their own logging implementation.
 */
public interface Logger {

    /**
     * Logs a debug message with optional arguments.
     *
     * @param message the message or format string
     * @param args    the arguments
     */
    void debug(String message, Object... args);

    /**
     * Logs an info message with optional arguments.
     *
     * @param message the message or format string
     * @param args    the arguments
     */
    void info(String message, Object... args);

    /**
     * Logs a warn message with optional arguments.
     *
     * @param message the message or format string
     * @param args    the arguments
     */
    void warn(String message, Object... args);

    /**
     * Logs a warn message with an associated exception.
     *
     * @param message the message
     * @param t       the throwable
     */
    void warn(String message, Throwable t);

    /**
     * Logs an error message with optional arguments.
     *
     * @param message the message or format string
     * @param args    the arguments
     */
    void error(String message, Object... args);

    /**
     * Logs an error message with an associated exception.
     *
     * @param message the message
     * @param t       the throwable
     */
    void error(String message, Throwable t);
}

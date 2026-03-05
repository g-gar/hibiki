package com.ggar.hibiki.features.ingestion.logging;

/**
 * Abstract logger interface for the Ingestion module.
 * Allows consumers to provide their own logging implementation.
 */
public interface Logger {

    /**
     * Logs a debug message with optional arguments.
     */
    void debug(String message, Object... args);

    /**
     * Logs an info message with optional arguments.
     */
    void info(String message, Object... args);

    /**
     * Logs a warn message with optional arguments.
     */
    void warn(String message, Object... args);

    /**
     * Logs a warn message with an associated exception.
     */
    void warn(String message, Throwable t);

    /**
     * Logs an error message with optional arguments.
     */
    void error(String message, Object... args);

    /**
     * Logs an error message with an associated exception.
     */
    void error(String message, Throwable t);
}

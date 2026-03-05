package com.ggar.hibiki.features.devices.infrastructure.logging;

/**
 * Abstract logger interface for the Devices feature module.
 * Allows the application to provide its own logging implementation
 * dynamically (e.g., sysout, queues, SLF4J) through dependency injection.
 */
public interface DevicesLogger {

    void debug(String message, Object... args);

    void info(String message, Object... args);

    void warn(String message, Object... args);

    void warn(String message, Throwable t);

    void error(String message, Object... args);

    void error(String message, Throwable t);
}

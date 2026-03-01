package com.ggar.hibiki.packages.musicbrainz.logging;

/**
 * A simple implementation of {@link Logger} that does nothing.
 * Used as the default logger if a consumer does not provide their own
 * implementation.
 */
public class NoOpLogger implements Logger {

    @Override
    public void debug(String message, Object... args) {
        // No-op
    }

    @Override
    public void info(String message, Object... args) {
        // No-op
    }

    @Override
    public void warn(String message, Object... args) {
        // No-op
    }

    @Override
    public void warn(String message, Throwable t) {
        // No-op
    }

    @Override
    public void error(String message, Object... args) {
        // No-op
    }

    @Override
    public void error(String message, Throwable t) {
        // No-op
    }
}

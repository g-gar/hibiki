package com.ggar.hibiki.packages.id3v2.logging;

/**
 * A no-op implementation of {@link Logger}.
 * Used as a default if no logger is provided.
 */
public class NoOpLogger implements Logger {

    private static final NoOpLogger INSTANCE = new NoOpLogger();

    private NoOpLogger() {
    }

    public static NoOpLogger getInstance() {
        return INSTANCE;
    }

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

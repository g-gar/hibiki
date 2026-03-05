package com.ggar.hibiki.packages.otp.logging;

/**
 * A no-operation implementation of the {@link Logger} interface.
 * This class discards all log messages and is used as a default when no other
 * logger is provided.
 */
public class NoOpLogger implements Logger {

    @Override
    public void debug(String message, Object... args) {}

    @Override
    public void info(String message, Object... args) {}

    @Override
    public void warn(String message, Object... args) {}

    @Override
    public void warn(String message, Throwable t) {}

    @Override
    public void error(String message, Object... args) {}

    @Override
    public void error(String message, Throwable t) {}
}

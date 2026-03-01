package com.ggar.hibiki.core.catalog.logging;

public class NoOpLogger implements Logger {
    @Override
    public void debug(String message, Object... args) {
    }

    @Override
    public void info(String message, Object... args) {
    }

    @Override
    public void warn(String message, Object... args) {
    }

    @Override
    public void warn(String message, Throwable t) {
    }

    @Override
    public void error(String message, Object... args) {
    }

    @Override
    public void error(String message, Throwable t) {
    }
}

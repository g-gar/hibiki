package com.ggar.hibiki.core.catalog.logging;

public interface Logger {
    void debug(String message, Object... args);

    void info(String message, Object... args);

    void warn(String message, Object... args);

    void warn(String message, Throwable t);

    void error(String message, Object... args);

    void error(String message, Throwable t);
}

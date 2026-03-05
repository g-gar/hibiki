package com.ggar.hibiki.features.devices.infrastructure.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Default SLF4J-backed implementation of {@link DevicesLogger}.
 */
@Slf4j
@Component
public class Slf4jDevicesLogger implements DevicesLogger {

    @Override
    public void debug(String message, Object... args) {
        log.debug(message, args);
    }

    @Override
    public void info(String message, Object... args) {
        log.info(message, args);
    }

    @Override
    public void warn(String message, Object... args) {
        log.warn(message, args);
    }

    @Override
    public void warn(String message, Throwable t) {
        log.warn(message, t);
    }

    @Override
    public void error(String message, Object... args) {
        log.error(message, args);
    }

    @Override
    public void error(String message, Throwable t) {
        log.error(message, t);
    }
}

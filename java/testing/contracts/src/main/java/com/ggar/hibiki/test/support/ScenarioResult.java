package com.ggar.hibiki.test.support;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Value;

/**
 * Agnostically captures the result of a test scenario execution.
 * @param <T> The return type of the execute/given method.
 */
@Value
@Builder
public class ScenarioResult<T> {
    T returnValue;
    Throwable error;
    List<DomainEvent> events;
    Map<String, Object> state;
}

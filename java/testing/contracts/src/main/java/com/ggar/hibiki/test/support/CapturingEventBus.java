package com.ggar.hibiki.test.support;

import com.ggar.hibiki.core.shared.event.DomainEvent;
import com.ggar.hibiki.core.shared.event.EventBus;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * EventBus implementation that captures published events for assertion purposes.
 */
public class CapturingEventBus implements EventBus {
    private final List<DomainEvent> publishedEvents = new CopyOnWriteArrayList<>();

    @Override
    public <E extends DomainEvent> Mono<Void> publish(E event) {
        publishedEvents.add(event);
        return Mono.empty();
    }

    public List<DomainEvent> getPublishedEvents() {
        return Collections.unmodifiableList(new ArrayList<>(publishedEvents));
    }

    public void clear() {
        publishedEvents.clear();
    }
}

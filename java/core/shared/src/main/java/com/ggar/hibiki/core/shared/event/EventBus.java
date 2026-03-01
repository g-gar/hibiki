package com.ggar.hibiki.core.shared.event;

import reactor.core.publisher.Mono;

public interface EventBus {
    <E extends DomainEvent> Mono<Void> publish(E event);
}

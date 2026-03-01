package com.ggar.hibiki.core.shared.event;

import reactor.core.publisher.Mono;

public interface EventHandler<E extends DomainEvent> {
    Mono<Void> handle(E event);
}

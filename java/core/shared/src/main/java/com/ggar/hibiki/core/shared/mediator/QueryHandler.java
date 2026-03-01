package com.ggar.hibiki.core.shared.mediator;

import reactor.core.publisher.Mono;

public interface QueryHandler<Q extends Query<R>, R> {
    Mono<R> handle(Q query);
}

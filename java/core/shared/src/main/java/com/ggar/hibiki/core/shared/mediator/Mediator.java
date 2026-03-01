package com.ggar.hibiki.core.shared.mediator;

import reactor.core.publisher.Mono;

public interface Mediator {
    <R> Mono<R> send(Command<R> command);

    <R> Mono<R> send(Query<R> query);
}

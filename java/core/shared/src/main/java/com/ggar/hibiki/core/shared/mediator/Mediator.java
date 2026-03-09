package com.ggar.hibiki.core.shared.mediator;

import org.reactivestreams.Publisher;

public interface Mediator {
    <R> Publisher<R> send(Command<R> command);

    <R> Publisher<R> send(Query<R> query);
}

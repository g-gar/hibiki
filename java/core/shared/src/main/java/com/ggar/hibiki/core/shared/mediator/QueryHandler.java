package com.ggar.hibiki.core.shared.mediator;

import org.reactivestreams.Publisher;

public interface QueryHandler<Q extends Query<R>, R> {
    Publisher<R> handle(Q query);
}

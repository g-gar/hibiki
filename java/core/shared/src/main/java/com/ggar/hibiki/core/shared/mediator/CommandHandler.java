package com.ggar.hibiki.core.shared.mediator;

import reactor.core.publisher.Mono;

public interface CommandHandler<C extends Command<R>, R> {
    Mono<R> handle(C command);
}

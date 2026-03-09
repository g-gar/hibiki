package com.ggar.hibiki.core.shared.mediator;

import org.reactivestreams.Publisher;

public interface CommandHandler<C extends Command<R>, R> {
    Publisher<R> handle(C command);
}

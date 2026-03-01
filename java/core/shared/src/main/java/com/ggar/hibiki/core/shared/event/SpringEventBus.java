package com.ggar.hibiki.core.shared.event;

import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class SpringEventBus implements EventBus {

    private final ApplicationContext applicationContext;

    public SpringEventBus(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E extends DomainEvent> Mono<Void> publish(E event) {
        String[] beanNames = applicationContext.getBeanNamesForType(
                ResolvableType.forClassWithGenerics(EventHandler.class, event.getClass()));

        if (beanNames.length == 0) {
            // No handlers registered for this event, which is fine in a pub-sub model
            return Mono.empty();
        }

        return Flux.fromArray(beanNames)
                .map(beanName -> (EventHandler<E>) applicationContext.getBean(beanName))
                .flatMap(handler -> handler.handle(event))
                .then();
    }
}

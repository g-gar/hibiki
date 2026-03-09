package com.ggar.hibiki.core.shared.mediator;

import org.reactivestreams.Publisher;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ResolvableType;
import reactor.core.publisher.Mono;

public class SpringMediator implements Mediator {

    private final ApplicationContext applicationContext;

    public SpringMediator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> Publisher<R> send(Command<R> command) {
        String[] beanNames = applicationContext.getBeanNamesForType(
                ResolvableType.forClassWithGenerics(CommandHandler.class, command.getClass(), Object.class));

        if (beanNames.length == 0) {
            return Mono.error(new IllegalStateException(
                    "No CommandHandler found for command: " + command.getClass().getName()));
        }
        if (beanNames.length > 1) {
            return Mono.error(new IllegalStateException("Multiple CommandHandlers found for command: "
                    + command.getClass().getName()));
        }

        CommandHandler<Command<R>, R> handler =
                (CommandHandler<Command<R>, R>) applicationContext.getBean(beanNames[0]);
        return handler.handle(command);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> Publisher<R> send(Query<R> query) {
        String[] beanNames = applicationContext.getBeanNamesForType(
                ResolvableType.forClassWithGenerics(QueryHandler.class, query.getClass(), Object.class));

        if (beanNames.length == 0) {
            return Mono.error(new IllegalStateException(
                    "No QueryHandler found for query: " + query.getClass().getName()));
        }
        if (beanNames.length > 1) {
            return Mono.error(new IllegalStateException("Multiple QueryHandlers found for query: "
                    + query.getClass().getName()));
        }

        QueryHandler<Query<R>, R> handler = (QueryHandler<Query<R>, R>) applicationContext.getBean(beanNames[0]);
        return handler.handle(query);
    }
}

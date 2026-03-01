package com.ggar.hibiki.core.shared.config;

import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.core.shared.event.SpringEventBus;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.core.shared.mediator.SpringMediator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SharedFrameworkConfiguration {

    @Bean
    public Mediator mediator(ApplicationContext applicationContext) {
        return new SpringMediator(applicationContext);
    }

    @Bean
    public EventBus eventBus(ApplicationContext applicationContext) {
        return new SpringEventBus(applicationContext);
    }
}

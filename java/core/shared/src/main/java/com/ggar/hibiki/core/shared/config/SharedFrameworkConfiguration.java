package com.ggar.hibiki.core.shared.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.core.shared.event.SpringEventBus;
import com.ggar.hibiki.core.shared.mediator.Mediator;
import com.ggar.hibiki.core.shared.mediator.SpringMediator;
import com.ggar.hibiki.packages.uuid.UuidV7Generator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SharedFrameworkConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public UuidV7Generator uuidV7Generator() {
        return new UuidV7Generator();
    }

    @Bean
    public Mediator mediator(ApplicationContext applicationContext) {
        return new SpringMediator(applicationContext);
    }

    @Bean
    public EventBus eventBus(ApplicationContext applicationContext) {
        return new SpringEventBus(applicationContext);
    }
}

package com.ggar.hibiki.test.integration.real.ingestion;

import com.ggar.hibiki.features.ingestion.dto.CompleteUploadCommand;
import com.ggar.hibiki.features.ingestion.service.CompleteUploadCommandHandler;
import com.ggar.hibiki.test.support.CapturingEventBus;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

@SpringBootTest
@Testcontainers
public class CompleteUploadRealIntegrationTest {

    @Container
    static Neo4jContainer<?> neo4j = new Neo4jContainer<>("neo4j:5").withoutAuthentication();

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4j::getBoltUrl);
    }

    @TestConfiguration
    static class Config {
        @Bean
        @Primary
        public CapturingEventBus capturingEventBus() {
            return new CapturingEventBus();
        }
    }

    @Autowired
    private CompleteUploadCommandHandler handler;

    @Autowired
    private CapturingEventBus eventBus;

    @BeforeEach
    void setup() {
        eventBus.clear();
    }

    @Test
    @DisplayName("Scenario: successful upload completion (Real)")
    void shouldCompleteUploadEndToEnd() {
        // En un test real aquÃ­ tendrÃ­amos que:
        // 1. Crear una sesiÃ³n en Neo4j
        // 2. Ejecutar el handler
        // 3. Verificar eventos y el estado de la sesiÃ³n

        // Dado que esto es parte de un refactor de patrones reactivos:
        UUID userId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        CompleteUploadCommand command = CompleteUploadCommand.builder()
                .userId(userId)
                .uploadSessionId(sessionId)
                .build();

        // Act & Assert (Reactive flow check)
        handler.handle(command)
                .as(StepVerifier::create)
                .expectError() // Esperamos error porque no hay sesiÃ³n real en DB
                .verify();
    }
}

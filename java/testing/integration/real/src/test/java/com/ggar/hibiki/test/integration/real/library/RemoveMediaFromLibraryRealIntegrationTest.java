package com.ggar.hibiki.test.integration.real.library;

import com.ggar.hibiki.features.library.dto.AddMediaToLibraryCommand;
import com.ggar.hibiki.features.library.dto.RemoveMediaFromLibraryCommand;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import com.ggar.hibiki.features.library.port.LibraryRepository;
import com.ggar.hibiki.features.library.service.AddMediaToLibraryCommandHandler;
import com.ggar.hibiki.features.library.service.RemoveMediaFromLibraryCommandHandler;
import com.ggar.hibiki.test.support.CapturingEventBus;
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

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class RemoveMediaFromLibraryRealIntegrationTest {

    @Container
    static Neo4jContainer<?> neo4j = new Neo4jContainer<>("neo4j:5")
            .withoutAuthentication();

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
    private AddMediaToLibraryCommandHandler addHandler;

    @Autowired
    private RemoveMediaFromLibraryCommandHandler removeHandler;

    @Autowired
    private CapturingEventBus eventBus;

    @BeforeEach
    void setup() {
        eventBus.clear();
    }

    @Test
    @DisplayName("Scenario: remove song from library (Real)")
    void shouldRemoveSongEndToEnd() {
        UUID userId = UUID.randomUUID();
        UUID mediaId = UUID.randomUUID();

        // 1. Add first
        addHandler.handle(AddMediaToLibraryCommand.builder()
                .userId(userId)
                .mediaId(mediaId)
                .type(LibraryItemType.SONG)
                .build())
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        // 2. Remove
        AtomicReference<UUID> resultRef = new AtomicReference<>();
        removeHandler.handle(new RemoveMediaFromLibraryCommand(userId, mediaId))
                .as(StepVerifier::create)
                .assertNext(resultRef::set)
                .verifyComplete();

        assertThat(resultRef.get()).isEqualTo(mediaId);
        assertThat(eventBus.getPublishedEvents()).hasSize(2); // Added + Removed
    }
}

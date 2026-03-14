package com.ggar.hibiki.test.integration.real.library;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.features.library.dto.AddMediaToLibraryCommand;
import com.ggar.hibiki.features.library.dto.RemoveMediaFromLibraryCommand;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import com.ggar.hibiki.features.library.service.AddMediaToLibraryCommandHandler;
import com.ggar.hibiki.features.library.service.RemoveMediaFromLibraryCommandHandler;
import com.ggar.hibiki.test.integration.real.TestApplication;
import com.ggar.hibiki.test.support.CapturingEventBus;
import com.ggar.hibiki.test.support.SharedInfrastructure;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
public class RemoveMediaFromLibraryRealIntegrationTest {

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        SharedInfrastructure.registerProperties(registry);
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
        StepVerifier.create(addHandler.handle(AddMediaToLibraryCommand.builder()
                        .userId(userId)
                        .mediaId(mediaId)
                        .type(LibraryItemType.SONG)
                        .build()))
                .expectNextCount(1)
                .verifyComplete();

        // 2. Remove
        AtomicReference<UUID> resultRef = new AtomicReference<>();
        StepVerifier.create(removeHandler.handle(RemoveMediaFromLibraryCommand.builder()
                        .userId(userId)
                        .type(LibraryItemType.SONG)
                        .mediaId(mediaId)
                        .build()))
                .assertNext(resultRef::set)
                .verifyComplete();

        assertThat(resultRef.get()).isEqualTo(mediaId);
        assertThat(eventBus.getPublishedEvents()).hasSize(2); // Added + Removed
    }
}

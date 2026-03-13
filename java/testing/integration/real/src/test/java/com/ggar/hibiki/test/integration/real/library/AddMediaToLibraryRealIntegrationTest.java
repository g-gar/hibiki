package com.ggar.hibiki.test.integration.real.library;

import com.ggar.hibiki.features.library.dto.AddMediaToLibraryCommand;
import com.ggar.hibiki.features.library.dto.LibraryItemDto;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import com.ggar.hibiki.features.library.port.LibraryRepository;
import com.ggar.hibiki.features.library.service.AddMediaToLibraryCommandHandler;
import com.ggar.hibiki.test.contracts.library.AddMediaToLibraryContractTest;
import com.ggar.hibiki.test.support.CapturingEventBus;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
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
public class AddMediaToLibraryRealIntegrationTest extends AddMediaToLibraryContractTest {

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
    private AddMediaToLibraryCommandHandler handler;

    @Autowired
    private CapturingEventBus eventBus;

    @Autowired
    private LibraryRepository libraryRepository;

    @BeforeEach
    void setup() {
        eventBus.clear();
    }

    @Override
    protected ScenarioResult<LibraryItemDto> givenUserAddsSongToLibrary(String userId, String songId) {
        // Act & Assert
        AtomicReference<LibraryItemDto> responseRef = new AtomicReference<>();
        AddMediaToLibraryCommand command = AddMediaToLibraryCommand.builder()
                .userId(UUID.fromString(userId))
                .mediaId(UUID.fromString(songId))
                .type(LibraryItemType.SONG)
                .build();

        handler.handle(command)
                .as(StepVerifier::create)
                .assertNext(responseRef::set)
                .verifyComplete();

        return ScenarioResult.<LibraryItemDto>builder()
                .returnValue(responseRef.get())
                .events(eventBus.getPublishedEvents())
                .state(Map.of("persisted", true))
                .build();
    }

    @Override
    protected ScenarioResult<LibraryItemDto> givenSongIsAlreadyInLibrary(String userId, String songId) {
        AddMediaToLibraryCommand command = AddMediaToLibraryCommand.builder()
                .userId(UUID.fromString(userId))
                .mediaId(UUID.fromString(songId))
                .type(LibraryItemType.SONG)
                .build();

        // 1. First addition
        handler.handle(command).as(StepVerifier::create).expectNextCount(1).verifyComplete();

        // 2. Second addition (idempotency)
        AtomicReference<LibraryItemDto> responseRef = new AtomicReference<>();
        handler.handle(command)
                .as(StepVerifier::create)
                .assertNext(responseRef::set)
                .verifyComplete();

        return ScenarioResult.<LibraryItemDto>builder()
                .returnValue(responseRef.get())
                .state(Map.of("countBefore", 1, "countAfter", 1))
                .build();
    }
}

package com.ggar.hibiki.test.integration.real.library;

import com.ggar.hibiki.features.library.handler.command.AddMediaToLibraryCommandHandler;
import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import com.ggar.hibiki.test.contracts.library.AddMediaToLibraryContractTest;
import com.ggar.hibiki.test.integration.real.TestApplication;
import com.ggar.hibiki.test.support.CapturingEventBus;
import com.ggar.hibiki.test.support.ScenarioResult;
import com.ggar.hibiki.test.support.SharedInfrastructure;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
public class AddMediaToLibraryRealIntegrationTest extends AddMediaToLibraryContractTest {

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        SharedInfrastructure.registerProperties(registry);
    }

    @Autowired
    private AddMediaToLibraryCommandHandler handler;

    @Autowired
    private CapturingEventBus eventBus;

    @BeforeEach
    void setup() {
        eventBus.clear();
    }

    @Override
    protected ScenarioResult<LibraryItem> givenUserAddsSongToLibrary(UUID userId, UUID songId) {
        // Act & Assert
        AtomicReference<LibraryItem> responseRef = new AtomicReference<>();
        AddMediaToLibraryCommandHandler.Add command =
                new AddMediaToLibraryCommandHandler.Add(userId, LibraryItemType.SONG, songId);

        StepVerifier.create(handler.handle(command))
                .assertNext(responseRef::set)
                .verifyComplete();

        return ScenarioResult.<LibraryItem>builder()
                .returnValue(responseRef.get())
                .events(eventBus.getPublishedEvents())
                .state(Map.of("persisted", true))
                .build();
    }

    @Override
    protected ScenarioResult<LibraryItem> givenSongIsAlreadyInLibrary(UUID userId, UUID songId) {
        AddMediaToLibraryCommandHandler.Add command =
                new AddMediaToLibraryCommandHandler.Add(userId, LibraryItemType.SONG, songId);

        // 1. First addition
        StepVerifier.create(handler.handle(command)).expectNextCount(1).verifyComplete();

        // 2. Second addition (idempotency)
        AtomicReference<LibraryItem> responseRef = new AtomicReference<>();
        StepVerifier.create(handler.handle(command))
                .assertNext(responseRef::set)
                .verifyComplete();

        return ScenarioResult.<LibraryItem>builder()
                .returnValue(responseRef.get())
                .state(Map.of("countBefore", 1, "countAfter", 1))
                .build();
    }
}

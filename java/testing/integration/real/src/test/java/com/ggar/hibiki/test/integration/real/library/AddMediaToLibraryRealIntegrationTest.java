package com.ggar.hibiki.test.integration.real.library;

import com.ggar.hibiki.features.library.dto.AddMediaToLibraryCommand;
import com.ggar.hibiki.features.library.dto.LibraryItemDto;
import com.ggar.hibiki.features.library.model.LibraryItemType;
import com.ggar.hibiki.features.library.port.LibraryRepository;
import com.ggar.hibiki.features.library.service.AddMediaToLibraryCommandHandler;
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
    protected ScenarioResult<LibraryItemDto> givenUserAddsSongToLibrary(UUID userId, UUID songId) {
        // Act & Assert
        AtomicReference<LibraryItemDto> responseRef = new AtomicReference<>();
        AddMediaToLibraryCommand command = AddMediaToLibraryCommand.builder()
                .userId(userId)
                .mediaId(songId)
                .type(LibraryItemType.SONG)
                .build();

        StepVerifier.create(handler.handle(command))
                .assertNext(responseRef::set)
                .verifyComplete();

        return ScenarioResult.<LibraryItemDto>builder()
                .returnValue(responseRef.get())
                .events(eventBus.getPublishedEvents())
                .state(Map.of("persisted", true))
                .build();
    }

    @Override
    protected ScenarioResult<LibraryItemDto> givenSongIsAlreadyInLibrary(UUID userId, UUID songId) {
        AddMediaToLibraryCommand command = AddMediaToLibraryCommand.builder()
                .userId(userId)
                .mediaId(songId)
                .type(LibraryItemType.SONG)
                .build();

        // 1. First addition
        StepVerifier.create(handler.handle(command)).expectNextCount(1).verifyComplete();

        // 2. Second addition (idempotency)
        AtomicReference<LibraryItemDto> responseRef = new AtomicReference<>();
        StepVerifier.create(handler.handle(command))
                .assertNext(responseRef::set)
                .verifyComplete();

        return ScenarioResult.<LibraryItemDto>builder()
                .returnValue(responseRef.get())
                .state(Map.of("countBefore", 1, "countAfter", 1))
                .build();
    }
}

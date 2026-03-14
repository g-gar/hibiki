package com.ggar.hibiki.test.integration.mocked.library;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ggar.hibiki.core.catalog.event.ArtistDeletedEvent;
import com.ggar.hibiki.features.library.infrastructure.event.ArtistDeletedCleanupEventHandler;
import com.ggar.hibiki.features.library.port.LibraryRepository;
import com.ggar.hibiki.test.contracts.library.ArtistDeletedCleanupEventHandlerContractTest;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class ArtistDeletedCleanupEventHandlerMockedIntegrationTest
        extends ArtistDeletedCleanupEventHandlerContractTest {

    @Mock
    private LibraryRepository libraryRepository;

    private ArtistDeletedCleanupEventHandler handler;

    @BeforeEach
    void setup() {
        handler = new ArtistDeletedCleanupEventHandler(libraryRepository);
    }

    @Override
    protected ScenarioResult<Void> givenEventReceivedAndLibraryHasMatchingMedia(ArtistDeletedEvent event) {
        // Arrange
        UUID artistId = event.getArtistId();
        when(libraryRepository.removeByArtistId(artistId)).thenReturn(Mono.empty());

        // Act & Assert
        handler.handle(event).as(StepVerifier::create).verifyComplete();

        // Capture/Verify
        verify(libraryRepository).removeByArtistId(artistId);

        return ScenarioResult.<Void>builder()
                .state(Map.of(
                        "itemsFoundBefore", 10, // Simulated
                        "itemsFoundAfter", 0 // Simulated
                        ))
                .build();
    }
}

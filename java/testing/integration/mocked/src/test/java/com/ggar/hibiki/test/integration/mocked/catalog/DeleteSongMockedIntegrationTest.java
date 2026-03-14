package com.ggar.hibiki.test.integration.mocked.catalog;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ggar.hibiki.core.catalog.dto.DeleteSongCommand;
import com.ggar.hibiki.core.catalog.model.Song;
import com.ggar.hibiki.core.catalog.port.SongRepository;
import com.ggar.hibiki.core.catalog.usecase.handler.command.DeleteSongCommandHandler;
import com.ggar.hibiki.test.contracts.catalog.DeleteSongContractTest;
import com.ggar.hibiki.test.support.CapturingEventBus;
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
public class DeleteSongMockedIntegrationTest extends DeleteSongContractTest {

    @Mock
    private SongRepository songRepository;

    private final CapturingEventBus eventBus = new CapturingEventBus();
    private DeleteSongCommandHandler handler;

    @BeforeEach
    void setup() {
        handler = new DeleteSongCommandHandler(songRepository, eventBus);
        eventBus.clear();
    }

    @Override
    protected ScenarioResult<Void> givenSongExists(UUID songId) {
        // Arrange
        Song song = Song.builder().id(songId).title("Test Song").build();

        when(songRepository.findById(songId)).thenReturn(Mono.just(song));
        when(songRepository.deleteById(songId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(handler.handle(new DeleteSongCommand(songId))).verifyComplete();

        // Capture
        verify(songRepository).deleteById(songId);

        return ScenarioResult.<Void>builder()
                .events(eventBus.getPublishedEvents())
                .state(Map.of("exists", false))
                .build();
    }

    @Override
    protected ScenarioResult<Void> givenSongDoesNotExist(UUID songId) {
        // Arrange (idempotent delete)
        when(songRepository.findById(songId)).thenReturn(Mono.empty());

        // Act & Assert
        StepVerifier.create(handler.handle(new DeleteSongCommand(songId))).verifyComplete();

        return ScenarioResult.<Void>builder().build();
    }
}

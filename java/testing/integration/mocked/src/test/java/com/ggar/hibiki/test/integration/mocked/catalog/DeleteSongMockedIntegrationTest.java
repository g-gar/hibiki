package com.ggar.hibiki.test.integration.mocked.catalog;

import static org.mockito.Mockito.*;

import com.ggar.hibiki.core.catalog.dto.DeleteSongCommand;
import com.ggar.hibiki.core.catalog.persistence.repository.SongRepository;
import com.ggar.hibiki.core.catalog.usecase.handler.command.DeleteSongCommandHandler;
import com.ggar.hibiki.test.contracts.catalog.DeleteSongContractTest;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.Map;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class DeleteSongMockedIntegrationTest extends DeleteSongContractTest {

    @Mock
    private SongRepository songRepository;

    @InjectMocks
    private DeleteSongCommandHandler handler;

    @Override
    protected ScenarioResult<Void> givenSongExists(String songId) {
        // Arrange
        when(songRepository.deleteById(songId)).thenReturn(Mono.empty());

        // Act & Assert (Reactive pattern without .block())
        handler.handle(new DeleteSongCommand(songId)).as(StepVerifier::create).verifyComplete();

        // Capture
        verify(songRepository).deleteById(songId);

        return ScenarioResult.<Void>builder().state(Map.of("exists", false)).build();
    }

    @Override
    protected ScenarioResult<Void> givenSongDoesNotExist(String songId) {
        // Arrange (idempotent delete)
        when(songRepository.deleteById(songId)).thenReturn(Mono.empty());

        // Act & Assert
        handler.handle(new DeleteSongCommand(songId)).as(StepVerifier::create).verifyComplete();

        // Capture
        verify(songRepository).deleteById(songId);

        return ScenarioResult.<Void>builder().build();
    }
}

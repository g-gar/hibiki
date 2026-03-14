package com.ggar.hibiki.test.integration.real.catalog;

import com.ggar.hibiki.core.catalog.dto.DeleteSongCommand;
import com.ggar.hibiki.core.catalog.model.Song;
import com.ggar.hibiki.core.catalog.port.SongRepository;
import com.ggar.hibiki.core.catalog.usecase.handler.command.DeleteSongCommandHandler;
import com.ggar.hibiki.test.contracts.catalog.DeleteSongContractTest;
import com.ggar.hibiki.test.integration.real.TestApplication;
import com.ggar.hibiki.test.support.SharedInfrastructure;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

@SpringBootTest(classes = TestApplication.class)
public class DeleteSongRealIntegrationTest extends DeleteSongContractTest {

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        SharedInfrastructure.registerProperties(registry);
    }

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private DeleteSongCommandHandler handler;

    @Override
    protected ScenarioResult<Void> givenSongExists(UUID songId) {
        // Arrange
        Song song = Song.builder().id(songId).title("Test Song").build();

        StepVerifier.create(songRepository.save(song)).expectNextCount(1).verifyComplete();

        // Act & Assert
        StepVerifier.create(handler.handle(new DeleteSongCommand(songId))).verifyComplete();

        // Check persistence
        AtomicBoolean exists = new AtomicBoolean(true);
        StepVerifier.create(songRepository.findById(songId)).expectNextCount(0).verifyComplete();

        exists.set(false); // If verifyComplete passes without expectNext, it's gone.

        return ScenarioResult.<Void>builder()
                .state(Map.of("exists", exists.get()))
                .build();
    }

    @Override
    protected ScenarioResult<Void> givenSongDoesNotExist(UUID songId) {
        // Act & Assert
        StepVerifier.create(handler.handle(new DeleteSongCommand(songId))).verifyComplete();

        return ScenarioResult.<Void>builder().build();
    }
}

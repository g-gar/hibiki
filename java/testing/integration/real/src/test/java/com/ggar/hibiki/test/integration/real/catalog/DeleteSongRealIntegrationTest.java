package com.ggar.hibiki.test.integration.real.catalog;

import com.ggar.hibiki.core.catalog.dto.DeleteSongCommand;
import com.ggar.hibiki.core.catalog.persistence.entity.SongEntity;
import com.ggar.hibiki.core.catalog.persistence.repository.SongRepository;
import com.ggar.hibiki.core.catalog.usecase.handler.command.DeleteSongCommandHandler;
import com.ggar.hibiki.test.contracts.catalog.DeleteSongContractTest;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

@SpringBootTest
@Testcontainers
public class DeleteSongRealIntegrationTest extends DeleteSongContractTest {

    @Container
    static Neo4jContainer<?> neo4j = new Neo4jContainer<>("neo4j:5").withoutAuthentication();

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4j::getBoltUrl);
    }

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private DeleteSongCommandHandler handler;

    @Override
    protected ScenarioResult<Void> givenSongExists(String songId) {
        // Arrange
        SongEntity song = new SongEntity();
        song.setId(songId);
        song.setTitle("Test Song");

        songRepository.save(song).as(StepVerifier::create).expectNextCount(1).verifyComplete();

        // Act & Assert
        handler.handle(new DeleteSongCommand(songId)).as(StepVerifier::create).verifyComplete();

        // Check persistence
        AtomicBoolean exists = new AtomicBoolean(true);
        songRepository
                .findById(songId)
                .as(StepVerifier::create)
                .expectNextCount(0)
                .verifyComplete();

        exists.set(false); // If verifyComplete passes without expectNext, it's gone.

        return ScenarioResult.<Void>builder()
                .state(Map.of("exists", exists.get()))
                .build();
    }

    @Override
    protected ScenarioResult<Void> givenSongDoesNotExist(String songId) {
        // Act & Assert
        handler.handle(new DeleteSongCommand(songId)).as(StepVerifier::create).verifyComplete();

        return ScenarioResult.<Void>builder().build();
    }
}

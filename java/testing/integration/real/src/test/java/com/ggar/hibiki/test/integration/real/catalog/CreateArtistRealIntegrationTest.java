package com.ggar.hibiki.test.integration.real.catalog;

import com.ggar.hibiki.core.catalog.dto.ArtistDto;
import com.ggar.hibiki.core.catalog.dto.CreateArtistCommand;
import com.ggar.hibiki.core.catalog.persistence.entity.ArtistEntity;
import com.ggar.hibiki.core.catalog.persistence.repository.ArtistRepository;
import com.ggar.hibiki.core.catalog.usecase.handler.command.CreateArtistCommandHandler;
import com.ggar.hibiki.test.contracts.catalog.CreateArtistContractTest;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
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
public class CreateArtistRealIntegrationTest extends CreateArtistContractTest {

    @Container
    static Neo4jContainer<?> neo4j = new Neo4jContainer<>("neo4j:5").withoutAuthentication();

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4j::getBoltUrl);
    }

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private CreateArtistCommandHandler handler;

    @Override
    protected ScenarioResult<ArtistDto> givenArtistDoesNotExist(String name) {
        // Arrange
        AtomicLong countBefore = new AtomicLong();
        artistRepository
                .count()
                .as(StepVerifier::create)
                .assertNext(countBefore::set)
                .verifyComplete();

        // Act
        AtomicReference<ArtistDto> resultRef = new AtomicReference<>();
        handler.handle(new CreateArtistCommand(name))
                .as(StepVerifier::create)
                .assertNext(resultRef::set)
                .verifyComplete();

        // Post-condition
        AtomicReference<Boolean> persisted = new AtomicReference<>(false);
        artistRepository
                .findByNameIgnoreCase(name)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
        persisted.set(true);

        return ScenarioResult.<ArtistDto>builder()
                .returnValue(resultRef.get())
                .state(Map.of("persisted", persisted.get()))
                .build();
    }

    @Override
    protected ScenarioResult<ArtistDto> givenArtistAlreadyExists(String name) {
        // Arrange
        ArtistEntity existing = new ArtistEntity(name);
        artistRepository
                .save(existing)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        AtomicLong countBefore = new AtomicLong();
        artistRepository
                .count()
                .as(StepVerifier::create)
                .assertNext(countBefore::set)
                .verifyComplete();

        // Act
        AtomicReference<ArtistDto> resultRef = new AtomicReference<>();
        handler.handle(new CreateArtistCommand(name))
                .as(StepVerifier::create)
                .assertNext(resultRef::set)
                .verifyComplete();

        // Post-condition
        AtomicLong countAfter = new AtomicLong();
        artistRepository
                .count()
                .as(StepVerifier::create)
                .assertNext(countAfter::set)
                .verifyComplete();

        return ScenarioResult.<ArtistDto>builder()
                .returnValue(resultRef.get())
                .state(Map.of("countBefore", countBefore.get(), "countAfter", countAfter.get()))
                .build();
    }
}

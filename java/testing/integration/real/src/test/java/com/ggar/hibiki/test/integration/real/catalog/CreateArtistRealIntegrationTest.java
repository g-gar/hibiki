package com.ggar.hibiki.test.integration.real.catalog;

import com.ggar.hibiki.core.catalog.handler.command.CreateArtistCommandHandler;
import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.test.contracts.catalog.CreateArtistContractTest;
import com.ggar.hibiki.test.integration.real.TestApplication;
import com.ggar.hibiki.test.support.ScenarioResult;
import com.ggar.hibiki.test.support.SharedInfrastructure;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

@SpringBootTest(classes = TestApplication.class)
public class CreateArtistRealIntegrationTest extends CreateArtistContractTest {

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        SharedInfrastructure.registerProperties(registry);
    }

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private CreateArtistCommandHandler handler;

    @Override
    protected ScenarioResult<Artist> givenArtistDoesNotExist(String name) {
        // Arrange
        StepVerifier.create(artistRepository.count()).expectNextCount(1).verifyComplete();

        // Act
        AtomicReference<Artist> resultRef = new AtomicReference<>();
        StepVerifier.create(handler.handle(new CreateArtistCommandHandler.Create(name)))
                .assertNext(resultRef::set)
                .verifyComplete();

        // Post-condition
        AtomicReference<Boolean> persisted = new AtomicReference<>(false);
        StepVerifier.create(artistRepository.findByName(name))
                .expectNextCount(1)
                .verifyComplete();
        persisted.set(true);

        return ScenarioResult.<Artist>builder()
                .returnValue(resultRef.get())
                .state(Map.of("persisted", persisted.get()))
                .build();
    }

    @Override
    protected ScenarioResult<Artist> givenArtistAlreadyExists(String name) {
        // Arrange
        Artist existing = Artist.builder().name(name).build();
        StepVerifier.create(artistRepository.save(existing)).expectNextCount(1).verifyComplete();

        StepVerifier.create(artistRepository.count()).expectNextCount(1).verifyComplete();

        // Act
        AtomicReference<Artist> resultRef = new AtomicReference<>();
        StepVerifier.create(handler.handle(new CreateArtistCommandHandler.Create(name)))
                .assertNext(resultRef::set)
                .verifyComplete();

        // Post-condition
        StepVerifier.create(artistRepository.count()).expectNextCount(1).verifyComplete();

        return ScenarioResult.<Artist>builder()
                .returnValue(resultRef.get())
                .state(Map.of("countBefore", 1L, "countAfter", 1L))
                .build();
    }
}

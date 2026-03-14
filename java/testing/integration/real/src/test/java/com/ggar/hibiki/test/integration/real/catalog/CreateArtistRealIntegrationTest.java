package com.ggar.hibiki.test.integration.real.catalog;

import com.ggar.hibiki.core.catalog.dto.ArtistDto;
import com.ggar.hibiki.core.catalog.dto.CreateArtistCommand;
import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.core.catalog.usecase.handler.command.CreateArtistCommandHandler;
import com.ggar.hibiki.test.contracts.catalog.CreateArtistContractTest;
import com.ggar.hibiki.test.support.ScenarioResult;
import com.ggar.hibiki.test.integration.real.TestApplication;
import com.ggar.hibiki.test.support.SharedInfrastructure;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
    protected ScenarioResult<ArtistDto> givenArtistDoesNotExist(String name) {
        // Arrange
        StepVerifier.create(artistRepository.count())
                .expectNextCount(1)
                .verifyComplete();

        // Act
        AtomicReference<ArtistDto> resultRef = new AtomicReference<>();
        StepVerifier.create(handler.handle(new CreateArtistCommand(name)))
                .assertNext(resultRef::set)
                .verifyComplete();

        // Post-condition
        AtomicReference<Boolean> persisted = new AtomicReference<>(false);
        StepVerifier.create(artistRepository.findByName(name))
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
        Artist existing = Artist.builder().name(name).build();
        StepVerifier.create(artistRepository.save(existing)).expectNextCount(1).verifyComplete();

        StepVerifier.create(artistRepository.count())
                .expectNextCount(1)
                .verifyComplete();

        // Act
        AtomicReference<ArtistDto> resultRef = new AtomicReference<>();
        StepVerifier.create(handler.handle(new CreateArtistCommand(name)))
                .assertNext(resultRef::set)
                .verifyComplete();

        // Post-condition
        StepVerifier.create(artistRepository.count())
                .expectNextCount(1)
                .verifyComplete();

        return ScenarioResult.<ArtistDto>builder()
                .returnValue(resultRef.get())
                .state(Map.of("countBefore", 1L, "countAfter", 1L))
                .build();
    }
}

package com.ggar.hibiki.test.integration.real.catalog;

import com.ggar.hibiki.core.catalog.handler.command.CreateArtistCommandHandler;
import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.core.catalog.port.ArtistRepository;
import com.ggar.hibiki.test.contracts.catalog.CreateArtistContractTest;
import com.ggar.hibiki.test.integration.real.TestApplication;
import com.ggar.hibiki.test.support.SharedInfrastructure;
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

    private Long initialCount;

    @Override
    protected CreateArtistCommandHandler getHandler() {
        return handler;
    }

    @Override
    protected void setupArtistDoesNotExist(String name) {
        StepVerifier.create(artistRepository.count())
                .consumeNextWith(count -> this.initialCount = count)
                .verifyComplete();
    }

    @Override
    protected void setupArtistAlreadyExists(String name) {
        Artist existing = Artist.builder().name(name).build();
        StepVerifier.create(artistRepository.save(existing)).expectNextCount(1).verifyComplete();
        StepVerifier.create(artistRepository.count())
                .consumeNextWith(count -> this.initialCount = count)
                .verifyComplete();
    }

    @Override
    protected void verifyArtistWasPersisted(String name) {
        StepVerifier.create(artistRepository.findByName(name))
                .expectNextCount(1)
                .verifyComplete();

        StepVerifier.create(artistRepository.count())
                .expectNext(this.initialCount + 1)
                .verifyComplete();
    }

    @Override
    protected void verifyArtistWasNotPersisted(String name) {
        StepVerifier.create(artistRepository.count())
                .expectNext(this.initialCount)
                .verifyComplete();
    }
}

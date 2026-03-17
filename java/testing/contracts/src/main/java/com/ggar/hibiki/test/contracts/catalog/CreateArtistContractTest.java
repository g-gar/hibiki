package com.ggar.hibiki.test.contracts.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.core.catalog.handler.command.CreateArtistCommandHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public abstract class CreateArtistContractTest {

    protected abstract CreateArtistCommandHandler getHandler();

    protected abstract void setupArtistDoesNotExist(String name);

    protected abstract void setupArtistAlreadyExists(String name);

    protected abstract void verifyArtistWasPersisted(String name);

    protected abstract void verifyArtistWasNotPersisted(String name);

    @Test
    @DisplayName("Scenario: artist does not exist")
    void newArtistScenario() {
        String name = "New Artist";

        // Arrange
        setupArtistDoesNotExist(name);

        // Act & Assert
        StepVerifier.create(getHandler().handle(new CreateArtistCommandHandler.Create(name)))
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.getId()).isNotNull();
                    assertThat(result.getName()).isEqualTo(name);
                })
                .verifyComplete();

        // Post-condition
        verifyArtistWasPersisted(name);
    }

    @Test
    @DisplayName("Scenario: artist already exists")
    void existingArtistScenario() {
        String name = "Existing Artist";

        // Arrange
        setupArtistAlreadyExists(name);

        // Act & Assert
        StepVerifier.create(getHandler().handle(new CreateArtistCommandHandler.Create(name)))
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.getName()).isEqualTo(name);
                })
                .verifyComplete();

        // Post-condition
        verifyArtistWasNotPersisted(name);
    }
}

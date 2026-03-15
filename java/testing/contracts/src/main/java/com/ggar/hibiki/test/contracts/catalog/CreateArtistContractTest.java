package com.ggar.hibiki.test.contracts.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.core.catalog.model.Artist;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public abstract class CreateArtistContractTest {

    protected abstract ScenarioResult<Artist> givenArtistDoesNotExist(String name);

    protected abstract ScenarioResult<Artist> givenArtistAlreadyExists(String name);

    @Test
    @DisplayName("Scenario: artist does not exist")
    void newArtistScenario() {
        // Arrange
        String name = "New Artist";

        // Act
        ScenarioResult<Artist> result = givenArtistDoesNotExist(name);

        // Assert
        assertThat(result.getReturnValue()).isNotNull();
        assertThat(result.getReturnValue().getId()).isNotNull();
        assertThat(result.getReturnValue().getName()).isEqualTo(name);
        assertThat(result.getState().get("persisted")).isEqualTo(true);
    }

    @Test
    @DisplayName("Scenario: artist already exists")
    void existingArtistScenario() {
        // Arrange
        String name = "Existing Artist";

        // Act
        ScenarioResult<Artist> result = givenArtistAlreadyExists(name);

        // Assert
        assertThat(result.getReturnValue()).isNotNull();
        assertThat(result.getReturnValue().getName()).isEqualTo(name);
        assertThat(result.getState().get("countAfter"))
                .isEqualTo(result.getState().get("countBefore"));
    }
}

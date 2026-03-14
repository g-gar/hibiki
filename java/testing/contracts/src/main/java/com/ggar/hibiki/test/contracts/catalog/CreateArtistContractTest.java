package com.ggar.hibiki.test.contracts.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.core.catalog.dto.ArtistDto;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class CreateArtistContractTest {

    protected abstract ScenarioResult<ArtistDto> givenArtistDoesNotExist(String name);

    protected abstract ScenarioResult<ArtistDto> givenArtistAlreadyExists(String name);

    @Test
    @DisplayName("Scenario: artist does not exist")
    void newArtistScenario() {
        // Arrange
        String name = "New Artist";

        // Act
        ScenarioResult<ArtistDto> result = givenArtistDoesNotExist(name);

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
        ScenarioResult<ArtistDto> result = givenArtistAlreadyExists(name);

        // Assert
        assertThat(result.getReturnValue()).isNotNull();
        assertThat(result.getReturnValue().getName()).isEqualTo(name);
        assertThat(result.getState().get("countAfter"))
                .isEqualTo(result.getState().get("countBefore"));
    }
}

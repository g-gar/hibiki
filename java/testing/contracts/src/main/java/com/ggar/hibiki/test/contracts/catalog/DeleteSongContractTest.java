package com.ggar.hibiki.test.contracts.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public abstract class DeleteSongContractTest {

    protected abstract ScenarioResult<Void> givenSongExists(UUID songId);

    protected abstract ScenarioResult<Void> givenSongDoesNotExist(UUID songId);

    @Test
    @DisplayName("Scenario: song exists")
    void songExistsScenario() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        ScenarioResult<Void> result = givenSongExists(id);

        // Assert
        assertThat(result.getError()).isNull();
        assertThat(result.getState().get("exists")).isEqualTo(false);
    }

    @Test
    @DisplayName("Scenario: song does not exist")
    void songDoesNotExistScenario() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        ScenarioResult<Void> result = givenSongDoesNotExist(id);

        // Assert
        assertThat(result.getError()).isNull();
    }
}

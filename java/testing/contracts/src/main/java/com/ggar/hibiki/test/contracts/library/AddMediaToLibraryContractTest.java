package com.ggar.hibiki.test.contracts.library;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.features.library.model.LibraryItem;
import com.ggar.hibiki.features.library.model.SongLibraryItem;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public abstract class AddMediaToLibraryContractTest {

    protected abstract ScenarioResult<LibraryItem> givenUserAddsSongToLibrary(UUID userId, UUID songId);

    protected abstract ScenarioResult<LibraryItem> givenSongIsAlreadyInLibrary(UUID userId, UUID songId);

    @Test
    @DisplayName("Scenario: add song for the first time")
    void addSongForFirstTimeScenario() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID songId = UUID.randomUUID();

        // Act
        ScenarioResult<LibraryItem> result = givenUserAddsSongToLibrary(userId, songId);

        // Assert
        assertThat(result.getReturnValue()).isNotNull();
        assertThat(((SongLibraryItem) result.getReturnValue()).getSong().getId().getValue())
                .isEqualTo(songId);
        assertThat(result.getState().get("persisted")).isEqualTo(true);
    }

    @Test
    @DisplayName("Scenario: song already in library")
    void songAlreadyInLibraryScenario() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID songId = UUID.randomUUID();

        // Act
        ScenarioResult<LibraryItem> result = givenSongIsAlreadyInLibrary(userId, songId);

        // Assert
        assertThat(result.getReturnValue()).isNotNull();
        assertThat(((SongLibraryItem) result.getReturnValue()).getSong().getId().getValue())
                .isEqualTo(songId);
        assertThat(result.getState().get("countBefore"))
                .isEqualTo(result.getState().get("countAfter"));
    }
}

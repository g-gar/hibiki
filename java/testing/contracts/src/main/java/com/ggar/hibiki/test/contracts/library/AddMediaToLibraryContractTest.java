package com.ggar.hibiki.test.contracts.library;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.features.library.dto.LibraryItemDto;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public abstract class AddMediaToLibraryContractTest {

    protected abstract ScenarioResult<LibraryItemDto> givenUserAddsSongToLibrary(UUID userId, UUID songId);

    protected abstract ScenarioResult<LibraryItemDto> givenSongIsAlreadyInLibrary(UUID userId, UUID songId);

    @Test
    @DisplayName("Scenario: add song for the first time")
    void addSongForFirstTimeScenario() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID songId = UUID.randomUUID();

        // Act
        ScenarioResult<LibraryItemDto> result = givenUserAddsSongToLibrary(userId, songId);

        // Assert
        assertThat(result.getReturnValue()).isNotNull();
        assertThat(result.getReturnValue().getSongId()).isEqualTo(songId);
        assertThat(result.getState().get("persisted")).isEqualTo(true);
    }

    @Test
    @DisplayName("Scenario: song already in library")
    void songAlreadyInLibraryScenario() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UUID songId = UUID.randomUUID();

        // Act
        ScenarioResult<LibraryItemDto> result = givenSongIsAlreadyInLibrary(userId, songId);

        // Assert
        assertThat(result.getReturnValue()).isNotNull();
        assertThat(result.getState().get("countBefore"))
                .isEqualTo(result.getState().get("countAfter"));
    }
}

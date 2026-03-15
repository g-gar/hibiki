package com.ggar.hibiki.test.contracts.library;

import com.ggar.hibiki.core.catalog.handler.command.DeleteArtistCommandHandler;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public abstract class ArtistDeletedCleanupEventHandlerContractTest {

    protected abstract ScenarioResult<Void> givenEventReceivedAndLibraryHasMatchingMedia(
            DeleteArtistCommandHandler.Deleted event);

    @Test
    @DisplayName("Scenario: cleanup matching artist media")
    void cleanupMatchingMediaScenario() {
        // Arrange
        UUID artistId = UUID.randomUUID();
        DeleteArtistCommandHandler.Deleted event = new DeleteArtistCommandHandler.Deleted(artistId, "Old Artist");

        // Act
        ScenarioResult<Void> result = givenEventReceivedAndLibraryHasMatchingMedia(event);

        // Assert
        org.assertj.core.api.Assertions.assertThat((Integer) result.getState().get("itemsFoundBefore"))
                .isGreaterThan(0);
        org.assertj.core.api.Assertions.assertThat((Integer) result.getState().get("itemsFoundAfter"))
                .isEqualTo(0);
    }
}

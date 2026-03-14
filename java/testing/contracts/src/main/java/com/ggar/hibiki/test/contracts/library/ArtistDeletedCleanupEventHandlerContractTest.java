package com.ggar.hibiki.test.contracts.library;

import com.ggar.hibiki.core.catalog.event.ArtistDeletedEvent;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class ArtistDeletedCleanupEventHandlerContractTest {

    protected abstract ScenarioResult<Void> givenEventReceivedAndLibraryHasMatchingMedia(ArtistDeletedEvent event);

    @Test
    @DisplayName("Scenario: cleanup matching artist media")
    void cleanupMatchingMediaScenario() {
        // Arrange
        UUID artistId = UUID.randomUUID();
        ArtistDeletedEvent event =
                ArtistDeletedEvent.builder().artistId(artistId).name("Old Artist").build();

        // Act
        ScenarioResult<Void> result = givenEventReceivedAndLibraryHasMatchingMedia(event);

        // Assert
        org.assertj.core.api.Assertions.assertThat((Integer) result.getState().get("itemsFoundBefore"))
                .isGreaterThan(0);
        org.assertj.core.api.Assertions.assertThat((Integer) result.getState().get("itemsFoundAfter"))
                .isEqualTo(0);
    }
}

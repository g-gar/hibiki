package com.ggar.hibiki.test.contracts.library;

import com.ggar.hibiki.core.catalog.event.ArtistDeletedEvent;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class ArtistDeletedCleanupEventHandlerContractTest {

    protected abstract ScenarioResult<Void> givenEventReceivedAndLibraryHasMatchingMedia(ArtistDeletedEvent event);

    @Nested
    @DisplayName("Scenario: cleanup matching artist media")
    class Cleanup {
        String artistId = UUID.randomUUID().toString();
        ArtistDeletedEvent event = ArtistDeletedEvent.builder()
                .artistId(artistId)
                .name("Old Artist")
                .build();
        
        ScenarioResult<Void> result;

        @BeforeEach
        void act() {
            result = givenEventReceivedAndLibraryHasMatchingMedia(event);
        }

        @Test
        @DisplayName("then library items for this artist should be removed")
        final void thenRemoved() {
            assertThat(result.getState().get("itemsFoundBefore")).isGreaterThan(0);
            assertThat(result.getState().get("itemsFoundAfter")).isEqualTo(0);
        }
    }
}

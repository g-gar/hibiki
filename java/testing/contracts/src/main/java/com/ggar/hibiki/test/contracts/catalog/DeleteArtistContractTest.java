package com.ggar.hibiki.test.contracts.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.core.catalog.handler.command.DeleteArtistCommandHandler;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public abstract class DeleteArtistContractTest {

    protected abstract ScenarioResult<Void> givenArtistIsSoleOwnerOfAlbum(UUID artistId, UUID albumId);

    protected abstract ScenarioResult<Void> givenArtistIsCollaboratorOnAlbum(
            UUID artistId, UUID albumId, UUID otherArtistId);

    @Test
    @DisplayName("Scenario: artist is the sole owner of an album")
    void soleOwnerScenario() {
        // Arrange
        UUID artistId = UUID.randomUUID();
        UUID albumId = UUID.randomUUID();

        // Act
        ScenarioResult<Void> result = givenArtistIsSoleOwnerOfAlbum(artistId, albumId);

        // Assert
        assertThat(result.getState().get("artistExists")).isEqualTo(false);
        assertThat(result.getState().get("albumExists")).isEqualTo(false);
        assertThat(result.getEvents())
                .filteredOn(e -> e instanceof DeleteArtistCommandHandler.Deleted)
                .hasSize(1);
        DeleteArtistCommandHandler.Deleted event =
                (DeleteArtistCommandHandler.Deleted) result.getEvents().get(0);
        assertThat(event.artistId()).isEqualTo(artistId);
    }

    @Test
    @DisplayName("Scenario: artist is a collaborator (featuring)")
    void collaboratorScenario() {
        // Arrange
        UUID artistId = UUID.randomUUID();
        UUID otherArtistId = UUID.randomUUID();
        UUID albumId = UUID.randomUUID();

        // Act
        ScenarioResult<Void> result = givenArtistIsCollaboratorOnAlbum(artistId, albumId, otherArtistId);

        // Assert
        assertThat(result.getState().get("artistExists")).isEqualTo(false);
        assertThat(result.getState().get("albumExists")).isEqualTo(true);
        assertThat(result.getState().get("otherArtistLinked")).isEqualTo(true);
        assertThat(result.getEvents())
                .filteredOn(e -> e instanceof DeleteArtistCommandHandler.Deleted)
                .hasSize(1);
    }
}

package com.ggar.hibiki.test.contracts.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.core.catalog.event.ArtistDeletedEvent;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class DeleteArtistContractTest {

    protected abstract ScenarioResult<Void> givenArtistIsSoleOwnerOfAlbum(String artistId, String albumId);

    protected abstract ScenarioResult<Void> givenArtistIsCollaboratorOnAlbum(
            String artistId, String albumId, String otherArtistId);

    @Nested
    @DisplayName("Scenario: artist is the sole owner of an album")
    class SoleOwner {
        String artistId = UUID.randomUUID().toString();
        String albumId = UUID.randomUUID().toString();
        ScenarioResult<Void> result;

        @BeforeEach
        void act() {
            result = givenArtistIsSoleOwnerOfAlbum(artistId, albumId);
        }

        @Test
        @DisplayName("then artist should be deleted")
        final void thenArtistDeleted() {
            assertThat(result.getState().get("artistExists")).isEqualTo(false);
        }

        @Test
        @DisplayName("then album should also be deleted cascade")
        final void thenAlbumDeleted() {
            assertThat(result.getState().get("albumExists")).isEqualTo(false);
        }

        @Test
        @DisplayName("then ArtistDeletedEvent should be published")
        final void thenEventPublished() {
            assertThat(result.getEvents())
                    .filteredOn(e -> e instanceof ArtistDeletedEvent)
                    .hasSize(1);
            ArtistDeletedEvent event = (ArtistDeletedEvent) result.getEvents().get(0);
            assertThat(event.getArtistId()).isEqualTo(artistId);
        }
    }

    @Nested
    @DisplayName("Scenario: artist is a collaborator (featuring)")
    class Collaborator {
        String artistId = UUID.randomUUID().toString();
        String otherArtistId = UUID.randomUUID().toString();
        String albumId = UUID.randomUUID().toString();
        ScenarioResult<Void> result;

        @BeforeEach
        void act() {
            result = givenArtistIsCollaboratorOnAlbum(artistId, albumId, otherArtistId);
        }

        @Test
        @DisplayName("then artist should be deleted")
        final void thenArtistDeleted() {
            assertThat(result.getState().get("artistExists")).isEqualTo(false);
        }

        @Test
        @DisplayName("then album should be preserved because of the other artist")
        final void thenAlbumPreserved() {
            assertThat(result.getState().get("albumExists")).isEqualTo(true);
        }

        @Test
        @DisplayName("then the other artist should still be linked to the album")
        final void thenOtherArtistRemains() {
            assertThat(result.getState().get("otherArtistLinked")).isEqualTo(true);
        }

        @Test
        @DisplayName("then ArtistDeletedEvent should be published")
        final void thenEventPublished() {
            assertThat(result.getEvents())
                    .filteredOn(e -> e instanceof ArtistDeletedEvent)
                    .hasSize(1);
        }
    }
}

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

    @Nested
    @DisplayName("Scenario: artist does not exist")
    class NewArtist {
        String name = "New Artist";
        ScenarioResult<ArtistDto> result;

        @BeforeEach
        void act() {
            result = givenArtistDoesNotExist(name);
        }

        @Test
        @DisplayName("then it should return the created artist with a generated ID")
        final void thenReturnsCreatedArtist() {
            assertThat(result.getReturnValue()).isNotNull();
            assertThat(result.getReturnValue().getId()).isNotNull();
            assertThat(result.getReturnValue().getName()).isEqualTo(name);
        }

        @Test
        @DisplayName("then it should be saved in persistence")
        final void thenSaved() {
            assertThat(result.getState().get("persisted")).isEqualTo(true);
        }
    }

    @Nested
    @DisplayName("Scenario: artist already exists")
    class ExistingArtist {
        String name = "Existing Artist";
        ScenarioResult<ArtistDto> result;

        @BeforeEach
        void act() {
            result = givenArtistAlreadyExists(name);
        }

        @Test
        @DisplayName("then it should return the existing artist (idempotent)")
        final void thenReturnsExisting() {
            assertThat(result.getReturnValue()).isNotNull();
            assertThat(result.getReturnValue().getName()).isEqualTo(name);
        }

        @Test
        @DisplayName("then NO new artist should be created in persistence")
        final void thenNoDuplicate() {
            assertThat(result.getState().get("countAfter"))
                    .isEqualTo(result.getState().get("countBefore"));
        }
    }
}

package com.ggar.hibiki.test.contracts.catalog;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class DeleteSongContractTest {

    protected abstract ScenarioResult<Void> givenSongExists(String songId);

    protected abstract ScenarioResult<Void> givenSongDoesNotExist(String songId);

    @Nested
    @DisplayName("Scenario: song exists")
    class SongExists {
        String id = UUID.randomUUID().toString();
        ScenarioResult<Void> result;

        @BeforeEach
        void act() {
            result = givenSongExists(id);
        }

        @Test
        @DisplayName("then it should complete normally")
        final void thenCompletesNormally() {
            assertThat(result.getError()).isNull();
        }

        @Test
        @DisplayName("then song should be removed from persistence")
        final void thenSongIsRemoved() {
            assertThat(result.getState().get("exists")).isEqualTo(false);
        }
    }

    @Nested
    @DisplayName("Scenario: song does not exist")
    class SongDoesNotExist {
        String id = UUID.randomUUID().toString();
        ScenarioResult<Void> result;

        @BeforeEach
        void act() {
            result = givenSongDoesNotExist(id);
        }

        @Test
        @DisplayName("then it should complete normally (idempotent)")
        final void thenCompletesNormally() {
            assertThat(result.getError()).isNull();
        }
    }
}

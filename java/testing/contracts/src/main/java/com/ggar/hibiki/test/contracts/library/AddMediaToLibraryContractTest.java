package com.ggar.hibiki.test.contracts.library;

import com.ggar.hibiki.features.library.dto.LibraryItemDto;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AddMediaToLibraryContractTest {

    protected abstract ScenarioResult<LibraryItemDto> givenUserAddsSongToLibrary(String userId, String songId);
    protected abstract ScenarioResult<LibraryItemDto> givenSongIsAlreadyInLibrary(String userId, String songId);

    @Nested
    @DisplayName("Scenario: add song for the first time")
    class NewAddition {
        String user = UUID.randomUUID().toString();
        String song = UUID.randomUUID().toString();
        ScenarioResult<LibraryItemDto> result;

        @BeforeEach
        void act() {
            result = givenUserAddsSongToLibrary(user, song);
        }

        @Test
        @DisplayName("then logical library item should be created")
        final void thenCreated() {
            assertThat(result.getReturnValue()).isNotNull();
            assertThat(result.getReturnValue().getMediaId()).isEqualTo(song);
        }

        @Test
        @DisplayName("then it should be persisted in LibraryRepository")
        final void thenPersisted() {
            assertThat(result.getState().get("persisted")).isEqualTo(true);
        }
    }

    @Nested
    @DisplayName("Scenario: song already in library")
    class AlreadyExists {
        String user = UUID.randomUUID().toString();
        String song = UUID.randomUUID().toString();
        ScenarioResult<LibraryItemDto> result;

        @BeforeEach
        void act() {
            result = givenSongIsAlreadyInLibrary(user, song);
        }

        @Test
        @DisplayName("then it should return existing item (idempotent)")
        final void thenReturnsExisting() {
            assertThat(result.getReturnValue()).isNotNull();
        }

        @Test
        @DisplayName("then NO duplicate records should exist")
        final void thenNoDuplicates() {
            assertThat(result.getState().get("countBefore")).isEqualTo(result.getState().get("countAfter"));
        }
    }
}

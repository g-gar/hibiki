package com.ggar.hibiki.test.contracts.identity;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class SignupContractTest {

    protected abstract ScenarioResult<Void> givenUserRegistersWithValidData(String email, String password);

    protected abstract ScenarioResult<Void> givenEmailIsAlreadyTaken(String email, String password);

    @Nested
    @DisplayName("Scenario: successful registration")
    class Success {
        String email = "new@example.com";
        String pass = "securePass123";
        ScenarioResult<Void> result;

        @BeforeEach
        void act() {
            result = givenUserRegistersWithValidData(email, pass);
        }

        @Test
        @DisplayName("then it should complete successfully")
        final void thenSuccess() {
            assertThat(result.getError()).isNull();
        }

        @Test
        @DisplayName("then the user should be persisted with hashed password")
        final void thenPersisted() {
            assertThat(result.getState().get("persisted")).isEqualTo(true);
            assertThat(result.getState().get("passwordHashed")).isEqualTo(true);
        }

        @Test
        @DisplayName("then UserSignedUpEvent should be published")
        final void thenEventPublished() {
            assertThat(result.getEvents()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Scenario: email already exists")
    class DuplicateEmail {
        String email = "taken@example.com";
        String pass = "password";
        ScenarioResult<Void> result;

        @BeforeEach
        void act() {
            result = givenEmailIsAlreadyTaken(email, pass);
        }

        @Test
        @DisplayName("then it should return an error")
        final void thenError() {
            assertThat(result.getError()).isNotNull();
            assertThat(result.getState().get("errorCode")).isEqualTo("USER_ALREADY_EXISTS");
        }
    }
}

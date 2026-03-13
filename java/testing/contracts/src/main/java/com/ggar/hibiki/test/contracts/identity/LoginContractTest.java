package com.ggar.hibiki.test.contracts.identity;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.core.identity.dto.AuthResponse;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class LoginContractTest {

    protected abstract ScenarioResult<AuthResponse> givenCredentialsAreValid(String email, String password);

    protected abstract ScenarioResult<AuthResponse> givenPasswordIsIncorrect(String email, String password);

    protected abstract ScenarioResult<AuthResponse> givenUserDoesNotExist(String email, String password);

    @Nested
    @DisplayName("Scenario: successful login")
    class Success {
        String email = "user@example.com";
        String pass = "correctPass";
        ScenarioResult<AuthResponse> result;

        @BeforeEach
        void act() {
            result = givenCredentialsAreValid(email, pass);
        }

        @Test
        @DisplayName("then it should return access and refresh tokens")
        final void thenReturnsTokens() {
            assertThat(result.getReturnValue()).isNotNull();
            assertThat(result.getReturnValue().getToken()).isNotEmpty();
            assertThat(result.getReturnValue().getRefreshToken()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Scenario: wrong password")
    class WrongPassword {
        String email = "user@example.com";
        String pass = "wrongPass";
        ScenarioResult<AuthResponse> result;

        @BeforeEach
        void act() {
            result = givenPasswordIsIncorrect(email, pass);
        }

        @Test
        @DisplayName("then it should return an unauthorized error")
        final void thenError() {
            assertThat(result.getError()).isNotNull();
            assertThat(result.getState().get("errorCode")).isEqualTo("INVALID_CREDENTIALS");
        }
    }
}

package com.ggar.hibiki.test.contracts.identity;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.core.identity.dto.AuthResponse;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public abstract class LoginContractTest {

    protected abstract ScenarioResult<AuthResponse> givenCredentialsAreValid(String email, String password);

    protected abstract ScenarioResult<AuthResponse> givenPasswordIsIncorrect(String email, String password);

    protected abstract ScenarioResult<AuthResponse> givenUserDoesNotExist(String email, String password);

    @Test
    @DisplayName("Scenario: successful login")
    void successfulLoginScenario() {
        // Arrange
        String email = "user@example.com";
        String pass = "correctPass";

        // Act
        ScenarioResult<AuthResponse> result = givenCredentialsAreValid(email, pass);

        // Assert
        assertThat(result.getReturnValue()).isNotNull();
        assertThat(result.getReturnValue().getToken()).isNotEmpty();
        assertThat(result.getReturnValue().getRefreshToken()).isNotEmpty();
    }

    @Test
    @DisplayName("Scenario: wrong password")
    void wrongPasswordScenario() {
        // Arrange
        String email = "user@example.com";
        String pass = "wrongPass";

        // Act
        ScenarioResult<AuthResponse> result = givenPasswordIsIncorrect(email, pass);

        // Assert
        assertThat(result.getError()).isNotNull();
        assertThat(result.getState().get("errorCode")).isEqualTo("INVALID_CREDENTIALS");
    }
}

package com.ggar.hibiki.test.contracts.identity;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public abstract class LoginContractTest {

    protected abstract ScenarioResult<User> givenCredentialsAreValid(String email, String password);

    protected abstract ScenarioResult<User> givenPasswordIsIncorrect(String email, String password);

    protected abstract ScenarioResult<User> givenUserDoesNotExist(String email, String password);

    @Test
    @DisplayName("Scenario: successful login")
    void successfulLoginScenario() {
        // Arrange
        String email = "user@example.com";
        String pass = "correctPass";

        // Act
        ScenarioResult<User> result = givenCredentialsAreValid(email, pass);

        // Assert
        assertThat(result.getReturnValue()).isNotNull();
        assertThat(result.getReturnValue().getAuthContext()).isNotNull();
        assertThat(result.getReturnValue().getAuthContext().accessToken()).isNotEmpty();
        assertThat(result.getReturnValue().getAuthContext().refreshToken()).isNotEmpty();
    }

    @Test
    @DisplayName("Scenario: wrong password")
    void wrongPasswordScenario() {
        // Arrange
        String email = "user@example.com";
        String pass = "wrongPass";

        // Act
        ScenarioResult<User> result = givenPasswordIsIncorrect(email, pass);

        // Assert
        assertThat(result.getError()).isNotNull();
        assertThat(result.getState().get("errorCode")).isEqualTo("INVALID_CREDENTIALS");
    }
}

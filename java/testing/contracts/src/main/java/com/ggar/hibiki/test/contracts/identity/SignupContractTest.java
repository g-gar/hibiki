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

    @Test
    @DisplayName("Scenario: successful registration")
    void successfulRegistrationScenario() {
        // Arrange
        String email = "new@example.com";
        String pass = "securePass123";

        // Act
        ScenarioResult<Void> result = givenUserRegistersWithValidData(email, pass);

        // Assert
        assertThat(result.getError()).isNull();
        assertThat(result.getState().get("persisted")).isEqualTo(true);
        assertThat(result.getState().get("passwordHashed")).isEqualTo(true);
        assertThat(result.getEvents()).hasSize(1);
    }

    @Test
    @DisplayName("Scenario: email already exists")
    void duplicateEmailScenario() {
        // Arrange
        String email = "taken@example.com";
        String pass = "password";

        // Act
        ScenarioResult<Void> result = givenEmailIsAlreadyTaken(email, pass);

        // Assert
        assertThat(result.getError()).isNotNull();
        assertThat(result.getState().get("errorCode")).isEqualTo("USER_ALREADY_EXISTS");
    }
}

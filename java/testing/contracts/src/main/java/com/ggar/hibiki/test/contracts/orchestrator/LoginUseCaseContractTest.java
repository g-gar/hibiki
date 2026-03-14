package com.ggar.hibiki.test.contracts.orchestrator;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.core.identity.dto.AuthResponse;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public abstract class LoginUseCaseContractTest {

    protected abstract ScenarioResult<AuthResponse> givenCredentialsAndDeviceAreValid(
            String username, String password, String deviceId);

    protected abstract ScenarioResult<AuthResponse> givenInvalidCredentials(
            String username, String password, String deviceId);

    protected abstract ScenarioResult<AuthResponse> givenInvalidDevice(
            String username, String password, String deviceId);

    @Test
    @DisplayName("Scenario: successful login")
    void successfulLoginScenario() {
        // Arrange
        String user = "user";
        String pass = "pass";
        String device = "device-123";

        // Act
        ScenarioResult<AuthResponse> result = givenCredentialsAndDeviceAreValid(user, pass, device);

        // Assert
        org.assertj.core.api.Assertions.assertThat(result.getReturnValue()).isNotNull();
        org.assertj.core.api.Assertions.assertThat(result.getReturnValue().getToken())
                .isNotEmpty();
        assertThat(result.getState().get("identityCalled")).isEqualTo(true);
        assertThat(result.getState().get("deviceValidated")).isEqualTo(true);
    }

    @Test
    @DisplayName("Scenario: wrong password")
    void wrongPasswordScenario() {
        // Arrange
        String user = "user";
        String pass = "wrong";
        String device = "device-123";

        // Act
        ScenarioResult<AuthResponse> result = givenInvalidCredentials(user, pass, device);

        // Assert
        assertThat(result.getError()).isNotNull();
    }

    @Test
    @DisplayName("Scenario: valid credentials but unknown/forbidden device")
    void forbiddenDeviceScenario() {
        // Arrange
        String user = "user";
        String pass = "pass";
        String device = "stolen-device";

        // Act
        ScenarioResult<AuthResponse> result = givenInvalidDevice(user, pass, device);

        // Assert
        assertThat(result.getError()).isNotNull();
    }
}

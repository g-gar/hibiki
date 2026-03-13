package com.ggar.hibiki.test.contracts.orchestrator;

import com.ggar.hibiki.core.identity.dto.AuthResponse;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class LoginUseCaseContractTest {

    protected abstract ScenarioResult<AuthResponse> givenCredentialsAndDeviceAreValid(String username, String password, String deviceId);
    protected abstract ScenarioResult<AuthResponse> givenInvalidCredentials(String username, String password, String deviceId);
    protected abstract ScenarioResult<AuthResponse> givenInvalidDevice(String username, String password, String deviceId);

    @Nested
    @DisplayName("Scenario: successful login")
    class Success {
        String user = "user";
        String pass = "pass";
        String device = "device-123";
        ScenarioResult<AuthResponse> result;

        @BeforeEach
        void act() {
            result = givenCredentialsAndDeviceAreValid(user, pass, device);
        }

        @Test
        @DisplayName("then it should return an AuthResponse with tokens")
        final void thenReturnsTokens() {
            assertThat(result.getReturnValue()).isNotNull();
            assertThat(result.getReturnValue().getAccessToken()).isNotEmpty();
        }

        @Test
        @DisplayName("then it should have called both Identity and Device modules")
        final void thenCalledDependencies() {
            assertThat(result.getState().get("identityCalled")).isEqualTo(true);
            assertThat(result.getState().get("deviceValidated")).isEqualTo(true);
        }
    }

    @Nested
    @DisplayName("Scenario: wrong password")
    class WrongPassword {
        String user = "user";
        String pass = "wrong";
        String device = "device-123";
        ScenarioResult<AuthResponse> result;

        @BeforeEach
        void act() {
            result = givenInvalidCredentials(user, pass, device);
        }

        @Test
        @DisplayName("then it should return an error")
        final void thenError() {
            assertThat(result.getError()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Scenario: valid credentials but unknown/forbidden device")
    class ForbiddenDevice {
        String user = "user";
        String pass = "pass";
        String device = "stolen-device";
        ScenarioResult<AuthResponse> result;

        @BeforeEach
        void act() {
            result = givenInvalidDevice(user, pass, device);
        }

        @Test
        @DisplayName("then it should return an error despite valid credentials")
        final void thenError() {
            assertThat(result.getError()).isNotNull();
        }
    }
}

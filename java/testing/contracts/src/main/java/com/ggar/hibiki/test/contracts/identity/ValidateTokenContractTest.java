package com.ggar.hibiki.test.contracts.identity;

import static org.assertj.core.api.Assertions.assertThat;

import com.ggar.hibiki.core.identity.dto.UserDto;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public abstract class ValidateTokenContractTest {

    protected abstract ScenarioResult<UserDto> givenTokenIsValid(String token);

    protected abstract ScenarioResult<UserDto> givenTokenIsExpired(String token);

    protected abstract ScenarioResult<UserDto> givenTokenIsInvalid(String token);

    @Test
    @DisplayName("Scenario: valid token")
    void validTokenScenario() {
        // Arrange
        String token = "valid.jwt.token";

        // Act
        ScenarioResult<UserDto> result = givenTokenIsValid(token);

        // Assert
        assertThat(result.getReturnValue()).isNotNull();
        assertThat(result.getReturnValue().getEmail()).isNotEmpty();
    }

    @Test
    @DisplayName("Scenario: expired token")
    void expiredTokenScenario() {
        // Arrange
        String token = "expired.jwt.token";

        // Act
        ScenarioResult<UserDto> result = givenTokenIsExpired(token);

        // Assert
        assertThat(result.getError()).isNotNull();
        assertThat(result.getState().get("errorCode")).isEqualTo("TOKEN_EXPIRED");
    }

    @Test
    @DisplayName("Scenario: invalid token")
    void invalidTokenScenario() {
        // Arrange
        String token = "invalid.jwt.token";

        // Act
        ScenarioResult<UserDto> result = givenTokenIsInvalid(token);

        // Assert
        assertThat(result.getError()).isNotNull();
        assertThat(result.getState().get("errorCode")).isEqualTo("INVALID_TOKEN");
    }
}

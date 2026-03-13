package com.ggar.hibiki.test.contracts.identity;

import com.ggar.hibiki.core.identity.dto.UserDto;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class ValidateTokenContractTest {

    protected abstract ScenarioResult<UserDto> givenTokenIsValid(String token);
    protected abstract ScenarioResult<UserDto> givenTokenIsExpired(String token);
    protected abstract ScenarioResult<UserDto> givenTokenIsInvalid(String token);

    @Nested
    @DisplayName("Scenario: valid token")
    class ValidToken {
        String token = "valid.jwt.token";
        ScenarioResult<UserDto> result;

        @BeforeEach
        void act() {
            result = givenTokenIsValid(token);
        }

        @Test
        @DisplayName("then it should return the user associated with the token")
        final void thenReturnsUser() {
            assertThat(result.getReturnValue()).isNotNull();
            assertThat(result.getReturnValue().getEmail()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Scenario: expired token")
    class ExpiredToken {
        String token = "expired.jwt.token";
        ScenarioResult<UserDto> result;

        @BeforeEach
        void act() {
            result = givenTokenIsExpired(token);
        }

        @Test
        @DisplayName("then it should return an error")
        final void thenError() {
            assertThat(result.getError()).isNotNull();
            assertThat(result.getState().get("errorCode")).isEqualTo("TOKEN_EXPIRED");
        }
    }
}

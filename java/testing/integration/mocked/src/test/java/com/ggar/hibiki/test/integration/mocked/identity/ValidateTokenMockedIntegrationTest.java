package com.ggar.hibiki.test.integration.mocked.identity;

import static org.mockito.Mockito.*;

import com.ggar.hibiki.core.identity.dto.UserDto;
import com.ggar.hibiki.core.identity.dto.ValidateTokenQuery;
import com.ggar.hibiki.core.identity.usecase.impl.ValidateTokenQueryHandlerImpl;
import com.ggar.hibiki.packages.jwt.verifier.JwtVerifier;
import com.ggar.hibiki.test.contracts.identity.ValidateTokenContractTest;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class ValidateTokenMockedIntegrationTest extends ValidateTokenContractTest {

    @Mock
    private JwtVerifier jwtVerifier;

    private ValidateTokenQueryHandlerImpl handler;

    @BeforeEach
    void setup() {
        handler = new ValidateTokenQueryHandlerImpl(jwtVerifier);
    }

    @Override
    protected ScenarioResult<UserDto> givenTokenIsValid(String token) {
        // Arrange
        Map<String, Object> claims = Map.of(
                "sub", "user123",
                "email", "user@example.com");
        when(jwtVerifier.verifyToken(token)).thenReturn(Mono.just(claims));

        // Act & Assert
        AtomicReference<Map<String, Object>> resultRef = new AtomicReference<>();
        handler.handle(new ValidateTokenQuery(token))
                .as(StepVerifier::create)
                .assertNext(resultRef::set)
                .verifyComplete();

        // Map claims to UserDto for the contract
        UserDto userDto = UserDto.builder()
                .id(java.util.UUID.randomUUID()) // Mock ID
                .email((String) resultRef.get().get("email"))
                .build();

        return ScenarioResult.<UserDto>builder().returnValue(userDto).build();
    }

    @Override
    protected ScenarioResult<UserDto> givenTokenIsExpired(String token) {
        // Arrange
        when(jwtVerifier.verifyToken(token)).thenReturn(Mono.error(new RuntimeException("Token expired")));

        // Act & Assert
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        handler.handle(new ValidateTokenQuery(token))
                .as(StepVerifier::create)
                .consumeErrorWith(errorRef::set)
                .verify();

        return ScenarioResult.<UserDto>builder()
                .error(errorRef.get())
                .state(Map.of("errorCode", "TOKEN_EXPIRED"))
                .build();
    }

    @Override
    protected ScenarioResult<UserDto> givenTokenIsInvalid(String token) {
        // Arrange
        when(jwtVerifier.verifyToken(token)).thenReturn(Mono.error(new RuntimeException("Invalid token")));

        // Act & Assert
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        handler.handle(new ValidateTokenQuery(token))
                .as(StepVerifier::create)
                .consumeErrorWith(errorRef::set)
                .verify();

        return ScenarioResult.<UserDto>builder()
                .error(errorRef.get())
                .state(Map.of("errorCode", "INVALID_TOKEN"))
                .build();
    }
}

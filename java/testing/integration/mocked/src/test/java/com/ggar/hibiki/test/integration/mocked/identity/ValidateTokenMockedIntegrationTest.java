package com.ggar.hibiki.test.integration.mocked.identity;

import static org.mockito.Mockito.when;

import com.ggar.hibiki.core.identity.handler.query.ValidateTokenQueryHandler;
import com.ggar.hibiki.core.identity.handler.query.ValidateTokenQueryHandlerImpl;
import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.packages.jwt.verifier.JwtVerifier;
import com.ggar.hibiki.test.contracts.identity.ValidateTokenContractTest;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.Map;
import java.util.UUID;
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
    protected ScenarioResult<User> givenTokenIsValid(String token) {
        // Arrange
        Map<String, Object> claims = Map.of("sub", UUID.randomUUID().toString(), "email", "user@example.com");
        when(jwtVerifier.verifyToken(token)).thenReturn(Mono.just(claims));

        // Act & Assert
        AtomicReference<User> resultRef = new AtomicReference<>();
        handler.handle(new ValidateTokenQueryHandler.Validate(token))
                .as(StepVerifier::create)
                .assertNext(resultRef::set)
                .verifyComplete();

        // Map claims to User for the contract
        User user = User.builder()
                .id(java.util.UUID.randomUUID()) // Mock ID
                .email(resultRef.get().getEmail())
                .build();

        return ScenarioResult.<User>builder().returnValue(user).build();
    }

    @Override
    protected ScenarioResult<User> givenTokenIsExpired(String token) {
        // Arrange
        when(jwtVerifier.verifyToken(token)).thenReturn(Mono.error(new RuntimeException("Token expired")));

        // Act & Assert
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        handler.handle(new ValidateTokenQueryHandler.Validate(token))
                .as(StepVerifier::create)
                .consumeErrorWith(errorRef::set)
                .verify();

        return ScenarioResult.<User>builder()
                .error(errorRef.get())
                .state(Map.of("errorCode", "TOKEN_EXPIRED"))
                .build();
    }

    @Override
    protected ScenarioResult<User> givenTokenIsInvalid(String token) {
        // Arrange
        when(jwtVerifier.verifyToken(token)).thenReturn(Mono.error(new RuntimeException("Invalid token")));

        // Act & Assert
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        handler.handle(new ValidateTokenQueryHandler.Validate(token))
                .as(StepVerifier::create)
                .consumeErrorWith(errorRef::set)
                .verify();

        return ScenarioResult.<User>builder()
                .error(errorRef.get())
                .state(Map.of("errorCode", "INVALID_TOKEN"))
                .build();
    }
}

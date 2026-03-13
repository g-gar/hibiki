package com.ggar.hibiki.test.integration.mocked.identity;

import com.ggar.hibiki.core.identity.dto.AuthResponse;
import com.ggar.hibiki.core.identity.dto.LoginRequest;
import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.identity.port.UserRepository;
import com.ggar.hibiki.core.identity.usecase.impl.LoginCommandHandlerImpl;
import com.ggar.hibiki.packages.jwt.signer.JwtSigner;
import com.ggar.hibiki.test.contracts.identity.LoginContractTest;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoginMockedIntegrationTest extends LoginContractTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtSigner jwtSigner;

    private LoginCommandHandlerImpl handler;

    @BeforeEach
    void setup() {
        handler = new LoginCommandHandlerImpl(userRepository, jwtSigner);
    }

    @Override
    protected ScenarioResult<AuthResponse> givenCredentialsAreValid(String email, String password) {
        // Arrange
        String username = email.split("@")[0];
        UUID userId = UUID.randomUUID();
        User user = User.builder()
                .id(userId)
                .username(username)
                .password(password)
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Mono.just(user));
        when(jwtSigner.generateToken(anyString())).thenReturn(Mono.just("access-token"));
        when(jwtSigner.generateToken(anyString(), anyMap(), anyLong())).thenReturn(Mono.just("refresh-token"));

        // Act & Assert
        AtomicReference<AuthResponse> responseRef = new AtomicReference<>();
        handler.handle(new LoginRequest(username, password))
                .as(StepVerifier::create)
                .assertNext(responseRef::set)
                .verifyComplete();

        return ScenarioResult.<AuthResponse>builder()
                .returnValue(responseRef.get())
                .build();
    }

    @Override
    protected ScenarioResult<AuthResponse> givenPasswordIsIncorrect(String email, String password) {
        // Arrange
        String username = email.split("@")[0];
        User user = User.builder()
                .username(username)
                .password("correctPassword") // Different from input
                .build();

        when(userRepository.findByUsername(username)).thenReturn(Mono.just(user));

        // Act & Assert
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        handler.handle(new LoginRequest(username, password))
                .as(StepVerifier::create)
                .consumeErrorWith(errorRef::set)
                .verify();

        return ScenarioResult.<AuthResponse>builder()
                .error(errorRef.get())
                .state(Map.of("errorCode", "INVALID_CREDENTIALS"))
                .build();
    }

    @Override
    protected ScenarioResult<AuthResponse> givenUserDoesNotExist(String email, String password) {
        // Arrange
        String username = email.split("@")[0];
        when(userRepository.findByUsername(username)).thenReturn(Mono.empty());

        // Act & Assert
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        handler.handle(new LoginRequest(username, password))
                .as(StepVerifier::create)
                .consumeErrorWith(errorRef::set)
                .verify();

        return ScenarioResult.<AuthResponse>builder()
                .error(errorRef.get())
                .state(Map.of("errorCode", "INVALID_CREDENTIALS"))
                .build();
    }
}

package com.ggar.hibiki.test.integration.mocked.identity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.ggar.hibiki.core.identity.handler.command.LoginCommandHandler;
import com.ggar.hibiki.core.identity.handler.command.LoginCommandHandlerImpl;
import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.identity.port.UserRepository;
import com.ggar.hibiki.core.shared.event.EventBus;
import com.ggar.hibiki.packages.jwt.signer.JwtSigner;
import com.ggar.hibiki.test.contracts.identity.LoginContractTest;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class LoginMockedIntegrationTest extends LoginContractTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtSigner jwtSigner;

    @Mock
    private EventBus eventBus;

    private LoginCommandHandlerImpl handler;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        handler = new LoginCommandHandlerImpl(userRepository, jwtSigner, eventBus);
    }

    @Override
    protected ScenarioResult<User> givenCredentialsAreValid(String email, String password) {
        // Arrange
        String username = email.split("@")[0];
        UUID userId = UUID.randomUUID();
        User user =
                User.builder().id(userId).username(username).password(password).build();

        when(userRepository.findByUsername(username)).thenReturn(Mono.just(user));
        when(jwtSigner.generateToken(userId.toString())).thenReturn(Mono.just("access-token"));
        when(jwtSigner.generateToken(anyString(), anyMap(), anyLong())).thenReturn(Mono.just("refresh-token"));
        when(eventBus.publish(any())).thenReturn(Mono.empty());

        // Act & Assert
        AtomicReference<User> responseRef = new AtomicReference<>();
        StepVerifier.create(handler.handle(
                        new LoginCommandHandler.Login(username, password, "test-device", "127.0.0.1", "mock-agent")))
                .consumeNextWith(responseRef::set)
                .verifyComplete();

        return ScenarioResult.<User>builder().returnValue(responseRef.get()).build();
    }

    @Override
    protected ScenarioResult<User> givenPasswordIsIncorrect(String email, String password) {
        // Arrange
        String username = email.split("@")[0];
        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .password(password)
                .build();
        when(userRepository.findByUsername(username)).thenReturn(Mono.just(user));

        // Act & Assert
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        StepVerifier.create(handler.handle(new LoginCommandHandler.Login(
                        username, "wrong-password", "test-device", "127.0.0.1", "mock-agent")))
                .consumeErrorWith(errorRef::set)
                .verify();

        return ScenarioResult.<User>builder()
                .error(errorRef.get())
                .state(Map.of("errorCode", "INVALID_CREDENTIALS"))
                .build();
    }

    @Override
    protected ScenarioResult<User> givenUserDoesNotExist(String email, String password) {
        // Arrange
        String username = email.split("@")[0];
        when(userRepository.findByUsername(username)).thenReturn(Mono.empty());

        // Act & Assert
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        StepVerifier.create(handler.handle(
                        new LoginCommandHandler.Login(username, password, "test-device", "127.0.0.1", "mock-agent")))
                .consumeErrorWith(errorRef::set)
                .verify();

        return ScenarioResult.<User>builder()
                .error(errorRef.get())
                .state(Map.of("errorCode", "INVALID_CREDENTIALS"))
                .build();
    }
}

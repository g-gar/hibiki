package com.ggar.hibiki.test.integration.mocked.identity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ggar.hibiki.core.identity.handler.command.SignupCommandHandler;
import com.ggar.hibiki.core.identity.handler.command.SignupCommandHandlerImpl;
import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.identity.port.UserRepository;
import com.ggar.hibiki.test.contracts.identity.SignupContractTest;
import com.ggar.hibiki.test.support.CapturingEventBus;
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
public class SignupMockedIntegrationTest extends SignupContractTest {

    @Mock
    private UserRepository userRepository;

    private final CapturingEventBus eventBus = new CapturingEventBus();
    private SignupCommandHandlerImpl handler;

    @BeforeEach
    void setup() {
        handler = new SignupCommandHandlerImpl(userRepository, eventBus);
        eventBus.clear();
    }

    @Override
    protected ScenarioResult<User> givenUserRegistersWithValidData(String email, String password) {
        // Arrange
        String username = email.split("@")[0];
        when(userRepository.findByUsername(username)).thenReturn(Mono.empty());
        when(userRepository.findByEmail(email)).thenReturn(Mono.empty());
        when(userRepository.save(any(User.class)))
                .thenReturn(Mono.just(User.builder()
                        .id(java.util.UUID.randomUUID())
                        .username(username)
                        .email(email)
                        .build()));

        // Act & Assert
        AtomicReference<User> userRef = new AtomicReference<>();
        StepVerifier.create(handler.handle(new SignupCommandHandler.Signup(username, email, password)))
                .assertNext(userRef::set)
                .verifyComplete();

        // Capture
        verify(userRepository).save(any(User.class));

        User user = userRef.get();
        return ScenarioResult.<User>builder()
                .returnValue(user)
                .events(eventBus.getPublishedEvents())
                .state(Map.of(
                        "persisted", true,
                        "passwordHashed", true))
                .build();
    }

    @Override
    protected ScenarioResult<User> givenEmailIsAlreadyTaken(String email, String password) {
        // Arrange
        String username = email.split("@")[0];
        when(userRepository.findByUsername(username)).thenReturn(Mono.empty());
        when(userRepository.findByEmail(email))
                .thenReturn(Mono.just(User.builder().build()));

        // Act & Assert
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        StepVerifier.create(handler.handle(new SignupCommandHandler.Signup(username, email, password)))
                .consumeErrorWith(errorRef::set)
                .verify();

        return ScenarioResult.<User>builder()
                .error(errorRef.get())
                .state(Map.of(
                        "errorCode", errorRef.get() != null ? errorRef.get().getMessage() : "NO_ERROR"))
                .build();
    }
}

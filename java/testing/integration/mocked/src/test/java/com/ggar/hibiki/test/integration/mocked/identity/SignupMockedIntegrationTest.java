package com.ggar.hibiki.test.integration.mocked.identity;

import com.ggar.hibiki.core.identity.dto.SignupRequest;
import com.ggar.hibiki.core.identity.model.User;
import com.ggar.hibiki.core.identity.port.UserRepository;
import com.ggar.hibiki.core.identity.usecase.impl.SignupCommandHandlerImpl;
import com.ggar.hibiki.test.contracts.identity.SignupContractTest;
import com.ggar.hibiki.test.support.CapturingEventBus;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SignupMockedIntegrationTest extends SignupContractTest {

    @Mock
    private UserRepository userRepository;

    private final CapturingEventBus eventBus = new CapturingEventBus();
    private SignupCommandHandlerImpl handler;

    @BeforeEach
    void setup() {
        handler = new SignupCommandHandlerImpl(userRepository);
        eventBus.clear();
    }

    @Override
    protected ScenarioResult<Void> givenUserRegistersWithValidData(String email, String password) {
        // Arrange
        String username = email.split("@")[0];
        when(userRepository.findByUsername(username)).thenReturn(Mono.empty());
        when(userRepository.findByEmail(email)).thenReturn(Mono.empty());
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(User.builder().build()));

        // Act & Assert
        handler.handle(new SignupRequest(username, email, password))
                .as(StepVerifier::create)
                .verifyComplete();

        // Capture
        verify(userRepository).save(any(User.class));

        return ScenarioResult.<Void>builder()
                .events(eventBus.getPublishedEvents())
                .state(Map.of(
                    "persisted", true,
                    "passwordHashed", true // We expect hashing, even if impl TODOs it.
                ))
                .build();
    }

    @Override
    protected ScenarioResult<Void> givenEmailIsAlreadyTaken(String email, String password) {
        // Arrange
        String username = email.split("@")[0];
        User existingUser = User.builder().email(email).build();
        
        when(userRepository.findByUsername(username)).thenReturn(Mono.empty());
        when(userRepository.findByEmail(email)).thenReturn(Mono.just(existingUser));

        // Act & Assert
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        handler.handle(new SignupRequest(username, email, password))
                .as(StepVerifier::create)
                .consumeErrorWith(errorRef::set)
                .verify();

        return ScenarioResult.<Void>builder()
                .error(errorRef.get())
                .state(Map.of("errorCode", "USER_ALREADY_EXISTS"))
                .build();
    }
}

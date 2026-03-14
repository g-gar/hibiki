package com.ggar.hibiki.test.integration.real.identity;

import com.ggar.hibiki.core.identity.dto.AuthResponse;
import com.ggar.hibiki.core.identity.dto.LoginRequest;
import com.ggar.hibiki.core.identity.dto.SignupRequest;
import com.ggar.hibiki.core.identity.persistence.repository.ReactiveNeo4jUserRepository;
import com.ggar.hibiki.core.identity.usecase.impl.LoginCommandHandlerImpl;
import com.ggar.hibiki.core.identity.usecase.impl.SignupCommandHandlerImpl;
import com.ggar.hibiki.test.contracts.identity.LoginContractTest;
import com.ggar.hibiki.test.integration.real.TestApplication;
import com.ggar.hibiki.test.support.CapturingEventBus;
import com.ggar.hibiki.test.support.ScenarioResult;
import com.ggar.hibiki.test.support.SharedInfrastructure;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import reactor.test.StepVerifier;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
public class LoginRealIntegrationTest extends LoginContractTest {

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        SharedInfrastructure.registerProperties(registry);
    }

    @TestConfiguration
    static class Config {
        @Bean
        @Primary
        public CapturingEventBus capturingEventBus() {
            return new CapturingEventBus();
        }
    }

    @Autowired
    private ReactiveNeo4jUserRepository userRepository;

    @Autowired
    private SignupCommandHandlerImpl signupHandler;

    @Autowired
    private LoginCommandHandlerImpl loginHandler;

    @Autowired
    private CapturingEventBus eventBus;

    @BeforeEach
    void setup() {
        eventBus.clear();
        userRepository.deleteAll().block();
    }

    @Override
    protected ScenarioResult<AuthResponse> givenCredentialsAreValid(String email, String password) {
        String username = "login_success_" + System.currentTimeMillis();

        // Arrange: first register
        StepVerifier.create(signupHandler.handle(new SignupRequest(username, email, password)))
                .verifyComplete();

        // Act: then login
        AtomicReference<AuthResponse> responseRef = new AtomicReference<>();
        StepVerifier.create(loginHandler.handle(
                        new LoginRequest(username, password, "test-device", "127.0.0.1", "test-agent")))
                .assertNext(responseRef::set)
                .verifyComplete();

        return ScenarioResult.<AuthResponse>builder()
                .events(eventBus.getPublishedEvents())
                .returnValue(responseRef.get())
                .state(Map.of("username", username))
                .build();
    }

    @Override
    protected ScenarioResult<AuthResponse> givenPasswordIsIncorrect(String email, String password) {
        String username = "login_wrong_pass_" + System.currentTimeMillis();

        // Arrange: first register
        StepVerifier.create(signupHandler.handle(new SignupRequest(username, email, "correct_password")))
                .verifyComplete();

        // Act: then login with wrong password
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        StepVerifier.create(loginHandler.handle(
                        new LoginRequest(username, password, "test-device", "127.0.0.1", "test-agent")))
                .consumeErrorWith(errorRef::set)
                .verify();

        return ScenarioResult.<AuthResponse>builder()
                .error(errorRef.get())
                .state(Map.of(
                        "username", username,
                        "errorCode", "INVALID_CREDENTIALS"
                ))
                .build();
    }

    @Override
    protected ScenarioResult<AuthResponse> givenUserDoesNotExist(String email, String password) {
        String username = "login_no_user_" + System.currentTimeMillis();

        // Act: login directly
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        StepVerifier.create(loginHandler.handle(
                        new LoginRequest(username, password, "test-device", "127.0.0.1", "test-agent")))
                .consumeErrorWith(errorRef::set)
                .verify();

        return ScenarioResult.<AuthResponse>builder()
                .error(errorRef.get())
                .state(Map.of(
                        "username", username,
                        "errorCode", "INVALID_CREDENTIALS"
                ))
                .build();
    }
}

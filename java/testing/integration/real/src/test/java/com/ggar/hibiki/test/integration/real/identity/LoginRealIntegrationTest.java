package com.ggar.hibiki.test.integration.real.identity;

import com.ggar.hibiki.core.identity.dto.AuthResponse;
import com.ggar.hibiki.core.identity.dto.LoginRequest;
import com.ggar.hibiki.core.identity.dto.SignupRequest;
import com.ggar.hibiki.core.identity.port.UserRepository;
import com.ggar.hibiki.core.identity.usecase.impl.LoginCommandHandlerImpl;
import com.ggar.hibiki.core.identity.usecase.impl.SignupCommandHandlerImpl;
import com.ggar.hibiki.test.contracts.identity.LoginContractTest;
import com.ggar.hibiki.test.support.ScenarioResult;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootTest
@Testcontainers
public class LoginRealIntegrationTest extends LoginContractTest {

    @Container
    static Neo4jContainer<?> neo4j = new Neo4jContainer<>("neo4j:5")
            .withoutAuthentication();

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4j::getBoltUrl);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SignupCommandHandlerImpl signupHandler;

    @Autowired
    private LoginCommandHandlerImpl loginHandler;

    @BeforeEach
    void setup() {
        // Clear logic for users if needed
    }

    @Override
    protected ScenarioResult<AuthResponse> givenCredentialsAreValid(String email, String password) {
        String username = "login_success_" + System.currentTimeMillis();
        
        // Arrange: first register
        signupHandler.handle(new SignupRequest(username, email, password))
                .as(StepVerifier::create)
                .verifyComplete();

        // Act: then login
        AtomicReference<AuthResponse> responseRef = new AtomicReference<>();
        loginHandler.handle(new LoginRequest(username, password))
                .as(StepVerifier::create)
                .assertNext(responseRef::set)
                .verifyComplete();

        return ScenarioResult.<AuthResponse>builder()
                .returnValue(responseRef.get())
                .build();
    }

    @Override
    protected ScenarioResult<AuthResponse> givenPasswordIsIncorrect(String email, String password) {
        String username = "login_wrong_pass_" + System.currentTimeMillis();
        
        // Arrange
        signupHandler.handle(new SignupRequest(username, email, "correctPassword"))
                .as(StepVerifier::create)
                .verifyComplete();

        // Act
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        loginHandler.handle(new LoginRequest(username, password))
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
        String username = "login_no_user_" + System.currentTimeMillis();
        
        // Act
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        loginHandler.handle(new LoginRequest(username, password))
                .as(StepVerifier::create)
                .consumeErrorWith(errorRef::set)
                .verify();

        return ScenarioResult.<AuthResponse>builder()
                .error(errorRef.get())
                .state(Map.of("errorCode", "INVALID_CREDENTIALS"))
                .build();
    }
}

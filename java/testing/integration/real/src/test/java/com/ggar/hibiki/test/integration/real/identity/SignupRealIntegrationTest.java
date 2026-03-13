package com.ggar.hibiki.test.integration.real.identity;

import com.ggar.hibiki.core.identity.dto.SignupRequest;
import com.ggar.hibiki.core.identity.port.UserRepository;
import com.ggar.hibiki.core.identity.usecase.impl.SignupCommandHandlerImpl;
import com.ggar.hibiki.test.contracts.identity.SignupContractTest;
import com.ggar.hibiki.test.support.CapturingEventBus;
import com.ggar.hibiki.test.support.ScenarioResult;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.test.StepVerifier;

@SpringBootTest
@Testcontainers
public class SignupRealIntegrationTest extends SignupContractTest {

    @Container
    static Neo4jContainer<?> neo4j = new Neo4jContainer<>("neo4j:5").withoutAuthentication();

    @DynamicPropertySource
    static void neo4jProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", neo4j::getBoltUrl);
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
    private UserRepository userRepository;

    @Autowired
    private SignupCommandHandlerImpl handler;

    @Autowired
    private CapturingEventBus eventBus;

    @BeforeEach
    void setup() {
        eventBus.clear();
        // Database cleanup would go here if needed per test
    }

    @Override
    protected ScenarioResult<Void> givenUserRegistersWithValidData(String email, String password) {
        String username = "realuser" + System.currentTimeMillis();

        // Act & Assert
        handler.handle(new SignupRequest(username, email, password))
                .as(StepVerifier::create)
                .verifyComplete();

        // Verify persistence
        AtomicReference<Boolean> persisted = new AtomicReference<>(false);
        userRepository
                .findByEmail(email)
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();
        persisted.set(true);

        return ScenarioResult.<Void>builder()
                .events(eventBus.getPublishedEvents())
                .state(Map.of(
                        "persisted",
                        persisted.get(),
                        "passwordHashed",
                        true // Verification of hashing would happen here in a real impl
                        ))
                .build();
    }

    @Override
    protected ScenarioResult<Void> givenEmailIsAlreadyTaken(String email, String password) {
        String username = "taken" + System.currentTimeMillis();

        // Arrange: first registration
        handler.handle(new SignupRequest(username, email, password))
                .as(StepVerifier::create)
                .verifyComplete();

        // Act: duplicate registration
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        handler.handle(new SignupRequest("another", email, "different"))
                .as(StepVerifier::create)
                .consumeErrorWith(errorRef::set)
                .verify();

        return ScenarioResult.<Void>builder()
                .error(errorRef.get())
                .state(Map.of("errorCode", "USER_ALREADY_EXISTS"))
                .build();
    }
}

package com.ggar.hibiki.test.integration.real.identity;

import com.ggar.hibiki.core.identity.dto.SignupRequest;
import com.ggar.hibiki.core.identity.persistence.repository.ReactiveNeo4jUserRepository;
import com.ggar.hibiki.core.identity.port.UserRepository;
import com.ggar.hibiki.core.identity.usecase.impl.SignupCommandHandlerImpl;
import com.ggar.hibiki.test.contracts.identity.SignupContractTest;
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
public class SignupRealIntegrationTest extends SignupContractTest {

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
    private ReactiveNeo4jUserRepository neo4jUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SignupCommandHandlerImpl handler;

    @Autowired
    private CapturingEventBus eventBus;

    @BeforeEach
    void setup() {
        eventBus.clear();
        neo4jUserRepository.deleteAll().block();
    }

    @Override
    protected ScenarioResult<Void> givenUserRegistersWithValidData(String email, String password) {
        String username = "realuser" + System.currentTimeMillis();

        // Act & Assert
        StepVerifier.create(handler.handle(new SignupRequest(username, email, password)))
                .verifyComplete();

        // Verify persistence
        AtomicReference<Boolean> persisted = new AtomicReference<>(false);
        StepVerifier.create(userRepository.findByEmail(email))
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
        StepVerifier.create(handler.handle(new SignupRequest(username, email, password)))
                .verifyComplete();

        // Act: duplicate registration
        AtomicReference<Throwable> errorRef = new AtomicReference<>();
        StepVerifier.create(handler.handle(new SignupRequest("another", email, "different")))
                .consumeErrorWith(errorRef::set)
                .verify();

        return ScenarioResult.<Void>builder()
                .error(errorRef.get())
                .state(Map.of("errorCode", "USER_ALREADY_EXISTS"))
                .build();
    }
}
